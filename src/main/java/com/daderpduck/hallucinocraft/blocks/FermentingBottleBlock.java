package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.blocks.entities.FermentingBottleBlockEntity;
import com.daderpduck.hallucinocraft.blocks.entities.ModBlockEntities;
import com.daderpduck.hallucinocraft.items.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FermentingBottleBlock extends BaseEntityBlock {
    public static final EnumProperty<Lid> LID = EnumProperty.create("lid", Lid.class);
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(0D, 0D, 0D, 16D, 14D, 16D), Block.box(6D, 14D, 6D, 10D, 16D, 10D));
    public enum Lid implements StringRepresentable {
        NONE("none"),
        CORK("cork"),
        AIRLOCK("airlock");

        private final String name;
        Lid(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public FermentingBottleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof FermentingBottleBlockEntity fermentingBottleBlockEntity)
                fermentingBottleBlockEntity.drops();
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof FermentingBottleBlockEntity fermentingBottleBlockEntity) {
            ItemStack itemStack = pPlayer.getItemInHand(pHand);

            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof BucketItem bucket) {
                    Fluid bucketFluid = bucket.getFluid();
                    Optional<IFluidHandler> optional = fermentingBottleBlockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve();

                    if (optional.isPresent()) {
                        IFluidHandler handler = optional.get();
                        Fluid tankFluid = handler.getFluidInTank(0).getFluid();

                        if (bucketFluid == Fluids.EMPTY) {
                            if (handler.getFluidInTank(0).getAmount() >= FluidAttributes.BUCKET_VOLUME) {
                                handler.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(itemStack, pPlayer, new ItemStack(bucketFluid.getBucket())));
                                pPlayer.awardStat(Stats.ITEM_USED.get(bucket));
                                pLevel.gameEvent(GameEvent.FLUID_PICKUP, pPos);

                                SoundEvent soundEvent = tankFluid.getAttributes().getFillSound();
                                if (soundEvent == null)
                                    soundEvent = tankFluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
                                pLevel.playSound(pPlayer, pPos, soundEvent, SoundSource.BLOCKS, 1F, 1F);

                                return InteractionResult.sidedSuccess(pLevel.isClientSide);
                            }
                        } else {
                            if (tankFluid == Fluids.EMPTY || handler.getFluidInTank(0).getAmount() + FluidAttributes.BUCKET_VOLUME <= handler.getTankCapacity(0)) {
                                handler.fill(new FluidStack(bucketFluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                                pPlayer.setItemInHand(pHand, BucketItem.getEmptySuccessItem(itemStack, pPlayer));
                                pPlayer.awardStat(Stats.ITEM_USED.get(bucket));
                                pLevel.gameEvent(GameEvent.FLUID_PLACE, pPos);

                                SoundEvent soundEvent = bucketFluid.getAttributes().getEmptySound();
                                if (soundEvent == null)
                                    soundEvent = bucketFluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
                                pLevel.playSound(pPlayer, pPos, soundEvent, SoundSource.BLOCKS, 1F, 1F);

                                return InteractionResult.sidedSuccess(pLevel.isClientSide);
                            }
                        }
                    }
                }

                if (pState.getValue(LID) == Lid.NONE) {
                    if (canUseAsLid(itemStack)) {
                        pPlayer.setItemInHand(pHand, fermentingBottleBlockEntity.insertLidItem(itemStack));
                        BlockState newState = pState.setValue(LID, getLidFromItem(itemStack));
                        pLevel.setBlock(pPos, newState, 3);
                        return InteractionResult.sidedSuccess(pLevel.isClientSide);
                    }
                }

                ItemStack itemStack1 = pushIngredient(pLevel, pPos, itemStack);
                if (itemStack1 != itemStack) {
                    pPlayer.setItemInHand(pHand, itemStack1);
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            } else if (pHand == InteractionHand.MAIN_HAND) {
                if (pPlayer.isCrouching() && pState.getValue(LID) != Lid.NONE) {
                    dropLid(pState, pLevel, pPos);
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }

                ItemStack itemStack1 = popIngredient(pLevel, pPos);
                if (itemStack1 != itemStack) {
                    spawnItem(pLevel, pPos, itemStack1);
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public static boolean canUseAsCork(ItemStack item) {
        return item.is(ItemTags.WOODEN_BUTTONS);
    }

    public static boolean canUseAsAirlock(ItemStack item) {
        return item.getItem() == ModItems.AIRLOCK.get();
    }

    public static boolean canUseAsLid(ItemStack item) {
        return canUseAsCork(item) || canUseAsAirlock(item);
    }

    public static Lid getLidFromItem(ItemStack item) {
        if (canUseAsCork(item)) {
            return Lid.CORK;
        } else if (canUseAsAirlock(item)) {
            return Lid.AIRLOCK;
        } else {
            return Lid.NONE;
        }
    }

    private void dropLid(BlockState state, Level level, BlockPos pos) {
        BlockState newState = state.setValue(LID, Lid.NONE);
        level.setBlock(pos, newState, 3);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FermentingBottleBlockEntity fermentingBottleBlockEntity) {
            ItemStack itemStack = fermentingBottleBlockEntity.getLidItem();
            if (itemStack.isEmpty()) return;
            fermentingBottleBlockEntity.emptyLidItem();
            spawnItem(level, pos, itemStack);
        }
    }

    private void spawnItem(Level level, BlockPos pos, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;
        double d0 = (double)(level.random.nextFloat() * 0.7F) + (double)0.15F;
        double d1 = (double)(level.random.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
        double d2 = (double)(level.random.nextFloat() * 0.7F) + (double)0.15F;
        ItemStack itemStack1 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(level, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemStack1);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    public ItemStack pushIngredient(Level level, BlockPos pos, ItemStack itemStack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FermentingBottleBlockEntity fermentingBottleBlockEntity) {
            return fermentingBottleBlockEntity.pushIngredient(itemStack);
        }

        return itemStack;
    }

    public ItemStack popIngredient(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FermentingBottleBlockEntity fermentingBottleBlockEntity) {
            return fermentingBottleBlockEntity.popIngredient();
        }

        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new FermentingBottleBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.FERMENTING_BOTTLE_BLOCK_ENTITY.get(), FermentingBottleBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LID);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1F;
    }
}

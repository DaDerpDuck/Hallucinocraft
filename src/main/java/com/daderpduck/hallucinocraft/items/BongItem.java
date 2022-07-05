package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.sounds.ModSounds;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BongItem extends Item {
    public static final Object2ObjectFunction<Item, ModItems.DrugChain> BONGABLES = new Object2ObjectLinkedOpenHashMap<>();

    public BongItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x4287F5;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) return InteractionResultHolder.pass(itemStack);

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        ItemStack offhandItem = player.getItemInHand(InteractionHand.OFF_HAND);

        if ((itemStack.getDamageValue() > 0 && hitResult.getType() == HitResult.Type.BLOCK && level.mayInteract(player, hitResult.getBlockPos()) && level.getFluidState(hitResult.getBlockPos()).is(FluidTags.WATER))
                && (hand == InteractionHand.OFF_HAND || offhandItem.isEmpty() || !BONGABLES.containsKey(offhandItem.getItem()) || itemStack.getDamageValue() == getMaxDamage(itemStack))) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, hitResult.getBlockPos());
            itemStack.setDamageValue(0);
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
        } else if (hand == InteractionHand.MAIN_HAND && !itemStack.isEmpty() && itemStack.getDamageValue() < getMaxDamage(itemStack) && !offhandItem.isEmpty() && BONGABLES.containsKey(offhandItem.getItem())) {
            player.startUsingItem(hand);
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
        }

        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack offhandItem = player.getItemInHand(InteractionHand.OFF_HAND);

            ModItems.DrugChain drugChain = BONGABLES.get(offhandItem.getItem());
            for (ModItems.DrugEffectProperty drugEffectProperty : drugChain.list) {
                Drug.addDrug(player, new DrugInstance(drugEffectProperty.drug().get(), drugEffectProperty.delayTicks(), drugEffectProperty.potencyPercentage(), drugEffectProperty.duration()));
            }

            if (!player.getAbilities().instabuild) {
                itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                offhandItem.shrink(1);
            }

            level.playSound(player, player.blockPosition(), ModSounds.BONG_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            player.awardStat(Stats.ITEM_USED.get(offhandItem.getItem()));
        }

        return itemStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public void fillItemCategory(CreativeModeTab itemGroup, NonNullList<ItemStack> itemStacks) {
        super.fillItemCategory(itemGroup, itemStacks);
        if (allowdedIn(itemGroup)) {
            ItemStack emptyBong = new ItemStack(this);
            emptyBong.setDamageValue(getMaxDamage(emptyBong));
            itemStacks.add(emptyBong);
        }
    }
}

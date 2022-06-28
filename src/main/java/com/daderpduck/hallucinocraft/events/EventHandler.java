package com.daderpduck.hallucinocraft.events;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hallucinocraft.MOD_ID)
public class EventHandler {
    @SubscribeEvent
    public static void cutPoppy(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        Level world = player.level;
        if (world.isClientSide) return;
        ItemStack itemStack = player.getItemInHand(hand);
        BlockPos blockPos = event.getHitVec().getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.is(Blocks.POPPY)) {
            if (itemStack.getItem() == Items.SHEARS) {
                world.playSound(null, blockPos, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1F, 1F);
                world.setBlock(blockPos, ModBlocks.CUT_POPPY_BLOCK.get().defaultBlockState(), 2);
                itemStack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
                player.swing(hand, true);
            }
        } else if (blockState.is(ModBlocks.CUT_POPPY_BLOCK.get()) && (blockState.getValue(ModBlocks.CUT_POPPY_BLOCK.get().getAgeProperty()) == 2)) {
            if (fillOpiumBottle(player, itemStack)) {
                world.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1F, 1F);
                world.destroyBlock(blockPos, false, player);
                player.swing(hand, true);
            }
        }
    }

    private static boolean fillOpiumBottle(Player player, ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item.equals(Items.GLASS_BOTTLE)) {
            itemStack.shrink(1);
            player.getInventory().add(ModItems.OPIUM_BOTTLE_0.get().getDefaultInstance());
            return true;
        } else if (item.equals(ModItems.OPIUM_BOTTLE_0.get())) {
            itemStack.shrink(1);
            player.getInventory().add(ModItems.OPIUM_BOTTLE_1.get().getDefaultInstance());
            return true;
        } else if (item.equals(ModItems.OPIUM_BOTTLE_1.get())) {
            itemStack.shrink(1);
            player.getInventory().add(ModItems.OPIUM_BOTTLE_2.get().getDefaultInstance());
            return true;
        } else if (item.equals(ModItems.OPIUM_BOTTLE_2.get())) {
            itemStack.shrink(1);
            player.getInventory().add(ModItems.OPIUM_BOTTLE_3.get().getDefaultInstance());
            return true;
        }
        return false;
    }
}

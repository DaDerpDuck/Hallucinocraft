package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.sounds.ModSounds;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BongItem extends Item implements Vanishable {
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
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        ItemStack itemStack = playerEntity.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && !itemStack.isEmpty() && itemStack.getDamageValue() < getMaxDamage(itemStack)) {
            ItemStack offhandItem = playerEntity.getItemInHand(InteractionHand.OFF_HAND);
            if (!offhandItem.isEmpty()) {
                if (BONGABLES.containsKey(offhandItem.getItem())) {
                    playerEntity.startUsingItem(hand);
                    return InteractionResultHolder.consume(itemStack);
                } else {
                    return InteractionResultHolder.fail(itemStack);
                }
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        if (entity instanceof Player playerEntity) {
            ItemStack offhandItem = playerEntity.getItemInHand(InteractionHand.OFF_HAND);

            ModItems.DrugChain drugChain = BONGABLES.get(offhandItem.getItem());
            for (ModItems.DrugEffectProperty drugEffectProperty : drugChain.list) {
                Drug.addDrug(playerEntity, new DrugInstance(drugEffectProperty.drug().get(), drugEffectProperty.delayTicks(), drugEffectProperty.potencyPercentage(), drugEffectProperty.duration()));
            }

            if (!playerEntity.getAbilities().instabuild) {
                itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                offhandItem.shrink(1);
            }

            world.playSound(playerEntity, playerEntity.blockPosition(), ModSounds.BONG_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
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

package com.daderpduck.psychedelicraft.items;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DrugItem extends Item {
    private final DrugEffectProperties[] effects;
    private final boolean edible;
    private final int useDuration;

    public DrugItem(Item.Properties properties) {
        super(properties);
        Properties drugProperties = (Properties)properties;

        effects = drugProperties.attachedDrugs.toArray(new DrugEffectProperties[]{});
        edible = drugProperties.edible;
        useDuration = drugProperties.useDuration;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (!playerEntity.isCreative())
                itemStack.shrink(1);

            for (DrugEffectProperties properties : effects) {
                if (properties.drug.isPresent()) {
                    Drug.addDrug(playerEntity, new DrugInstance(properties.drug.get(), properties.delayTick, properties.strength, properties.duration));
                } else {
                    Psychedelicraft.LOGGER.error("{} is not in the drug registry!", properties.drug.toString());
                }
            }
        }

        return itemStack;
    }

    @Override
    public boolean isEdible() {
        return super.isEdible() || edible;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (isEdible()) {
            ItemStack itemstack = player.getItemInHand(hand);
            player.startUsingItem(hand);
            return ActionResult.consume(itemstack);
        } else {
            return ActionResult.pass(player.getItemInHand(hand));
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        if (itemStack.getItem().isEdible()) {
            return useDuration;
        } else {
            return 0;
        }
    }

    public static class Properties extends Item.Properties {
        private final List<DrugEffectProperties> attachedDrugs = new ArrayList<>();
        private boolean edible = false;
        private int useDuration = 32;

        public Properties addDrug(RegistryObject<Drug> drugRegistryObject, int delayTicks, float strength, int duration) {
            this.attachedDrugs.add(new DrugEffectProperties(drugRegistryObject, delayTicks, strength, duration));
            return this;
        }

        public Properties edible() {
            this.edible = true;
            return this;
        }

        public Properties edible(int useDuration) {
            this.edible = true;
            this.useDuration = useDuration;
            return this;
        }
    }

    private static class DrugEffectProperties {
        private final RegistryObject<Drug> drug;
        private final int delayTick;
        private final float strength;
        private final int duration;

        public DrugEffectProperties(RegistryObject<Drug> drug, int delayTick, float strength, int duration) {
            this.drug = drug;
            this.delayTick = delayTick;
            this.strength = strength;
            this.duration = duration;
        }
    }
}

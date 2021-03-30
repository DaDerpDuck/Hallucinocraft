package com.daderpduck.psychedelicraft.items;

import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class DrugItem extends Item {
    private final DrugEffectProperties[] effects = new DrugEffectProperties[]{};

    public DrugItem(Item.Properties properties) {
        super(properties);
        ((Properties)properties).attachedDrugs.toArray(effects);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            itemStack.shrink(1);

            for (DrugEffectProperties properties : effects) {
                Drug.addDrug((PlayerEntity) entity, new DrugInstance(properties.drug, properties.delayTick, properties.strength));
            }
        }

        return itemStack;
    }

    public static class Properties extends Item.Properties {
        private final List<DrugEffectProperties> attachedDrugs = new ArrayList<>();

        public Properties addDrug(Drug drug, int delayTicks, float strength) {
            this.attachedDrugs.add(new DrugEffectProperties(drug, delayTicks, strength));
            return this;
        }
    }

    private static class DrugEffectProperties {
        private final Drug drug;
        private final int delayTick;
        private final float strength;

        public DrugEffectProperties(Drug drug, int delayTick, float strength) {
            this.drug = drug;
            this.delayTick = delayTick;
            this.strength = strength;
        }
    }
}

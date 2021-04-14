package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
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
    private final UseAction useAction;

    public DrugItem(Item.Properties properties) {
        super(properties);
        Properties drugProperties = (Properties)properties;

        effects = drugProperties.attachedDrugs.toArray(new DrugEffectProperties[]{});
        edible = drugProperties.edible;
        useDuration = drugProperties.useDuration;
        useAction = drugProperties.useAction;
    }

    protected void addDrugs(PlayerEntity playerEntity) {
        for (DrugEffectProperties properties : effects) {
            if (properties.drug.isPresent()) {
                Drug.addDrug(playerEntity, new DrugInstance(properties.drug.get(), properties.delayTick, properties.potencyPercentage*properties.drug.get().getMaxEffect(), properties.duration));
            } else {
                Hallucinocraft.LOGGER.error("{} is not in the drug registry!", properties.drug.toString());
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (!playerEntity.abilities.instabuild) itemStack.shrink(1);

            addDrugs(playerEntity);
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
    public UseAction getUseAnimation(ItemStack itemStack) {
        return useAction;
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
        private UseAction useAction = UseAction.EAT;

        public Properties addDrug(RegistryObject<Drug> drugRegistryObject, int delayTicks, float potencyPercentage, int duration) {
            this.attachedDrugs.add(new DrugEffectProperties(drugRegistryObject, delayTicks, potencyPercentage, duration));
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

        public Properties useAction(UseAction useAction) {
            this.useAction = useAction;
            return this;
        }
    }

    private static class DrugEffectProperties {
        private final RegistryObject<Drug> drug;
        private final int delayTick;
        private final float potencyPercentage;
        private final int duration;

        public DrugEffectProperties(RegistryObject<Drug> drug, int delayTick, float potencyPercentage, int duration) {
            this.drug = drug;
            this.delayTick = delayTick;
            this.potencyPercentage = potencyPercentage;
            this.duration = duration;
        }
    }
}

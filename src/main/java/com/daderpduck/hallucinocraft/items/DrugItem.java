package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DrugItem extends Item {
    private final DrugEffectProperties[] effects;
    private final UseAnim useAction;

    public DrugItem(Item.Properties properties) {
        super(properties);
        Properties drugProperties = (Properties)properties;

        effects = drugProperties.attachedDrugs.toArray(new DrugEffectProperties[]{});
        useAction = drugProperties.useAction;
    }

    protected void addDrugs(Player player) {
        for (DrugEffectProperties properties : effects) {
            if (properties.drug.isPresent()) {
                Drug.addDrug(player, new DrugInstance(properties.drug.get(), properties.delayTick, properties.potencyPercentage, properties.duration));
            } else {
                Hallucinocraft.LOGGER.error("{} is not in the drug registry!", properties.drug.toString());
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (isEdible()) {
                player.eat(level, itemStack);
            } else if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            addDrugs(player);
        }

        return itemStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (isEdible()) {
            return super.use(level, player, hand);
        } else {
            ItemStack itemstack = player.getItemInHand(hand);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return isEdible() ? super.getUseAnimation(itemStack) : useAction;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return isEdible() ? super.getUseDuration(itemStack) : 32;
    }

    public static class Properties extends Item.Properties {
        private final List<DrugEffectProperties> attachedDrugs = new ArrayList<>();
        private UseAnim useAction = UseAnim.EAT;

        public Properties addDrug(RegistryObject<Drug> drugRegistryObject, int delayTicks, float potencyPercentage, int duration) {
            this.attachedDrugs.add(new DrugEffectProperties(drugRegistryObject, delayTicks, potencyPercentage, duration));
            return this;
        }

        public Properties useAction(UseAnim useAction) {
            this.useAction = useAction;
            return this;
        }
    }

    private record DrugEffectProperties(RegistryObject<Drug> drug, int delayTick, float potencyPercentage, int duration) {
    }
}

package com.daderpduck.hallucinocraft.drugs;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Hallucinocraft.MOD_ID)
public class DrugHandler {
    private static final UUID uuid = UUID.fromString("512eebf1-6b63-4e4e-be79-42d90813a70a");

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        Drug.tick(player);

        modifyAttribute(player, Attributes.MOVEMENT_SPEED, "Drug movement speed", Math.max(DrugEffects.MOVEMENT_SPEED.getValue(), -0.5F), AttributeModifier.Operation.MULTIPLY_TOTAL);
        modifyAttribute(player, Attributes.ATTACK_SPEED, "Drug attack speed", DrugEffects.DIG_SPEED.getValue(), AttributeModifier.Operation.MULTIPLY_TOTAL);

        for (DrugEffects effect : DrugEffects.values()) {
            if (!effect.isClientOnly()) effect.resetValue();
        }
    }


    private static void modifyAttribute(LivingEntity entity, Attribute attribute, String name, double value, AttributeModifier.Operation op) {
        ModifiableAttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) return;
        attributeInstance.removeModifier(uuid);
        attributeInstance.addTransientModifier(new AttributeModifier(uuid, name, value, op));
    }
}

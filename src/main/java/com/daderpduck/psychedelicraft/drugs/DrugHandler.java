package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Psychedelicraft.MOD_ID)
public class DrugHandler {
    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        for (PlayerEntity player : event.world.players()) {
            Drug.tick(player);
        }
    }

}

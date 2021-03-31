package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Psychedelicraft.MOD_ID)
public class DrugHandler {
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Drug.tick(event.player);
    }

}

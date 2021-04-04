package com.daderpduck.psychedelicraft.client.rendering;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class DrugRenderer {
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        if (event.phase == TickEvent.Phase.START) {
            for (DrugInstance drugInstance : Drug.getDrugs(mc.player)) {
                drugInstance.getDrug().renderTick(drugInstance, drugInstance.getCurrentEffect(), event.renderTickTime);
            }

            for (DrugEffects effect : DrugEffects.values()) {
                //TODO: Handle camera tremble, hand tremble, modifiers...
            }
        } else {
            for (DrugEffects effect : DrugEffects.values()) {
                effect.resetValue();
            }
        }
    }
}

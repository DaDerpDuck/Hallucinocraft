package com.daderpduck.hallucinocraft.capabilities;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import static com.daderpduck.hallucinocraft.capabilities.PlayerDrugs.sync;
import static com.daderpduck.hallucinocraft.capabilities.PlayerDrugs.syncActives;

@Mod.EventBusSubscriber(modid = Hallucinocraft.MOD_ID)
public class PlayerCapEvents {
    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerDrugsProvider.PLAYER_DRUGS).isPresent()) {
                event.addCapability(new ResourceLocation(Hallucinocraft.MOD_ID, "drugs"), new PlayerDrugsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerDrugsProvider.PLAYER_DRUGS).ifPresent(oldStore ->
                    event.getPlayer().getCapability(PlayerDrugsProvider.PLAYER_DRUGS).ifPresent(newStore ->
                            newStore.copyFrom(oldStore)));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDrugs.class);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sync((ServerPlayer) event.getPlayer());
        syncActives((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync((ServerPlayer) event.getPlayer());
        syncActives((ServerPlayer) event.getPlayer());
    }

    private static int totalTicks = 1;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (totalTicks++ % (5*20) == 0) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    sync(player);
                }
            }
        }
    }
}

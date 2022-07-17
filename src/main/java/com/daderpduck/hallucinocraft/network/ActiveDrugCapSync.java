package com.daderpduck.hallucinocraft.network;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ActiveDrugCapSync implements IMessage {
    private final Map<Drug, Float> map;

    public ActiveDrugCapSync(Map<Drug, Float> activeDrugs) {
        map = activeDrugs;
    }

    public ActiveDrugCapSync(FriendlyByteBuf buffer) {
        map = new HashMap<>();

        int mapSize = buffer.readInt();
        for (int i = 0; i < mapSize; i++) {
            map.put(DrugRegistry.DRUGS.getValue(buffer.readResourceLocation()), buffer.readFloat());
        }
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(map.size());

        map.forEach((drug, effect) -> {
            packetBuffer.writeResourceLocation(Objects.requireNonNull(DrugRegistry.DRUGS.getKey(drug)));
            packetBuffer.writeFloat(effect);
        });
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            PlayerDrugs playerDrugs = PlayerDrugs.getPlayerDrugs(player);
            playerDrugs.setActives(map);
        });
        ctx.get().setPacketHandled(true);
    }
}

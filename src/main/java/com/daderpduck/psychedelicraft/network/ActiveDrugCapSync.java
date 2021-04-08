package com.daderpduck.psychedelicraft.network;

import com.daderpduck.psychedelicraft.capabilities.IPlayerDrugs;
import com.daderpduck.psychedelicraft.capabilities.PlayerProperties;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ActiveDrugCapSync implements IMessage {
    private final Map<Drug, Float> map;

    public ActiveDrugCapSync(Map<Drug, Float> activeDrugs) {
        map = activeDrugs;
    }

    public ActiveDrugCapSync(PacketBuffer buffer) {
        map = new HashMap<>();

        int mapSize = buffer.readInt();
        for (int i = 0; i < mapSize; i++) {
            map.put(DrugRegistry.DRUGS.getValue(buffer.readResourceLocation()), buffer.readFloat());
        }
    }

    @Override
    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(map.size());

        map.forEach((drug, effect) -> {
            packetBuffer.writeResourceLocation(Objects.requireNonNull(DrugRegistry.DRUGS.getKey(drug)));
            packetBuffer.writeFloat(effect);
        });
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;
            IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
            playerDrugs.setActives(map);
        });
        ctx.get().setPacketHandled(true);
    }
}

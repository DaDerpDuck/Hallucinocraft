package com.daderpduck.hallucinocraft.network;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DrugCapSync implements IMessage {
    private final List<DrugInstance> list;

    public DrugCapSync(List<DrugInstance> drugInstances) {
        list = drugInstances;
    }

    public DrugCapSync(FriendlyByteBuf buffer) {
        int listSize = buffer.readInt();
        list = new ArrayList<>(listSize);

        for (int i = 0; i < listSize; i++) {
            list.add(new DrugInstance(DrugRegistry.DRUGS.getValue(buffer.readResourceLocation()), buffer.readInt(), buffer.readFloat(), buffer.readInt(), buffer.readInt()));
        }
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(list.size());

        for (DrugInstance drugInstance : list) {
            packetBuffer.writeResourceLocation(Objects.requireNonNull(DrugRegistry.DRUGS.getKey(drugInstance.getDrug())));
            packetBuffer.writeInt(drugInstance.getDelayTime());
            packetBuffer.writeFloat(drugInstance.getPotency());
            packetBuffer.writeInt(drugInstance.getDuration());
            packetBuffer.writeInt(drugInstance.getTimeActive());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            PlayerDrugs playerDrugs = PlayerDrugs.getPlayerDrugs(player);
            playerDrugs.setSources(list);
        });
        ctx.get().setPacketHandled(true);
    }
}

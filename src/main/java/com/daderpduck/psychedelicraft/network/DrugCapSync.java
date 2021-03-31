package com.daderpduck.psychedelicraft.network;

import com.daderpduck.psychedelicraft.capabilities.IPlayerDrugs;
import com.daderpduck.psychedelicraft.capabilities.PlayerProperties;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;
import com.daderpduck.psychedelicraft.drugs.DrugRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DrugCapSync implements IMessage {
    private final ResourceLocation drugType;
    private final int delay;
    private final float current;
    private final float desired;

    public DrugCapSync(DrugInstance drugInstance) {
        this.drugType = DrugRegistry.DRUGS.getKey(drugInstance.getDrug());
        this.delay = drugInstance.getDelayTime();
        this.current = drugInstance.getCurrentEffect();
        this.desired = drugInstance.getDesiredEffect();
    }

    public DrugCapSync(PacketBuffer buffer) {
        this.drugType = buffer.readResourceLocation();
        this.delay = buffer.readInt();
        this.current = buffer.readFloat();
        this.desired = buffer.readFloat();
    }

    @Override
    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeResourceLocation(drugType);
        packetBuffer.writeInt(delay);
        packetBuffer.writeFloat(current);
        packetBuffer.writeFloat(desired);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;
            Drug drug = DrugRegistry.DRUGS.getValue(drugType);
            IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
            if (delay <= 0 && playerDrugs.hasDrug(drug)) {
                playerDrugs.setDrugDesiredEffect(drug, desired);
            } else {
                playerDrugs.overrideDrug(new DrugInstance(drug, delay, desired, current));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

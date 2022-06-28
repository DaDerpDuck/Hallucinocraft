package com.daderpduck.hallucinocraft.network;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Hallucinocraft.MOD_ID, "messages"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void init() {
        register(DrugCapSync.class, DrugCapSync::new);
        register(ActiveDrugCapSync.class, ActiveDrugCapSync::new);
    }

    private static int id = 0;
    private static <MSG extends IMessage> void register(Class<MSG> msg, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.registerMessage(id++, msg, IMessage::encode, decoder, IMessage::handle);
    }
}

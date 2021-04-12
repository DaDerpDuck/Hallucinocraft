package com.daderpduck.hallucinocraft;

import com.daderpduck.hallucinocraft.capabilities.PlayerProperties;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.commands.SetDrugCommand;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Hallucinocraft.MOD_ID)
public class Hallucinocraft {
    public static final String MOD_ID = "hallucinocraft";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Drug> DRUGS = DeferredRegister.create(Drug.class, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public Hallucinocraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DRUGS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);

        ModItems.init();

        PacketHandler.init();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerProperties.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ShaderRenderer.setup();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        /*InterModComms.sendTo("hallucinocraft", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });*/
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        /*LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));*/
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        SetDrugCommand.register(event.getServer().getCommands().getDispatcher());
    }
}

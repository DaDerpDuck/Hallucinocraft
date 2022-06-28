package com.daderpduck.hallucinocraft;

import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.commands.SetDrugCommand;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import com.daderpduck.hallucinocraft.items.BongRegistry;
import com.daderpduck.hallucinocraft.items.CompostRegistry;
import com.daderpduck.hallucinocraft.items.ModItems;
import com.daderpduck.hallucinocraft.network.PacketHandler;
import com.daderpduck.hallucinocraft.sounds.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Mod(Hallucinocraft.MOD_ID)
public class Hallucinocraft {
    public static final String MOD_ID = "hallucinocraft";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab TAB = new CreativeModeTab("creativetab") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return ModItems.RED_SHROOMS.get().getDefaultInstance();
        }
    };

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final DeferredRegister<Drug> DRUGS = DeferredRegister.create(new ResourceLocation(Hallucinocraft.MOD_ID, "drug"), MOD_ID);

    private static Supplier<IForgeRegistry<Drug>> drugSupplier;

    public Hallucinocraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        drugSupplier = DRUGS.makeRegistry(Drug.class, DrugRegistry::getRegistryBuilder);

        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        SOUNDS.register(modEventBus);
        DRUGS.register(modEventBus);

        ModItems.init(modEventBus);
        ModBlocks.init();
        ModSounds.init();

        PacketHandler.init();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DrugRegistry.DRUGS = drugSupplier.get(); //TODO: Is this correct??
        CompostRegistry.register();
        BongRegistry.register();
        SetDrugCommand.registerSerializer();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModBlocks.initRenderTypes();

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
    public void onRegisterCommands(RegisterCommandsEvent event) {
        SetDrugCommand.register(event.getDispatcher());
    }
}

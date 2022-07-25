package com.daderpduck.hallucinocraft.datagen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.items.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("empty_bong", new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/empty_bong"));
        withExistingParent("filled_bong", new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/filled_bong"));
        withExistingParent(getItemName(ModItems.BONG.get()), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/empty_bong"))
                .override()
                .predicate(new ResourceLocation("damage"), 0)
                .model(new ModelFile.ExistingModelFile(new ResourceLocation(Hallucinocraft.MOD_ID, "item/filled_bong"), existingFileHelper))
                .end()
                .override()
                .predicate(new ResourceLocation("damage"), 1)
                .model(new ModelFile.ExistingModelFile(new ResourceLocation(Hallucinocraft.MOD_ID, "item/empty_bong"), existingFileHelper))
                .end();
        simpleItem(ModItems.EMPTY_SYRINGE.get());
        simpleItem(ModItems.BROWN_SHROOMS.get());
        simpleItem(ModItems.DRIED_BROWN_MUSHROOM.get());
        simpleItem(ModItems.RED_SHROOMS.get());
        simpleItem(ModItems.DRIED_RED_MUSHROOM.get());
        withExistingParent(getItemName(ModItems.SHROOM_STEW.get()), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation("item/mushroom_stew"));
        simpleItem(ModItems.COCA_LEAF.get());
        simpleItem(ModItems.COCA_MULCH.get());
        simpleItem(ModItems.COCA_SEEDS.get());
        simpleItem(ModItems.COCAINE_ROCK.get());
        simpleItem(ModItems.COCAINE_DUST.get());
        simpleItem(ModItems.COCAINE_POWDER.get());
        withExistingParent(getItemName(ModItems.COKE_CAKE.get()), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation("item/cake"));
        simpleItem(ModItems.CANNABIS_BUD.get());
        simpleItem(ModItems.CANNABIS_JOINT.get());
        simpleItem(ModItems.CANNABIS_LEAF.get());
        simpleItem(ModItems.CANNABIS_SEEDS.get());
        simpleItem(ModItems.DRIED_CANNABIS_BUD.get());
        simpleItem(ModItems.DRIED_CANNABIS_LEAF.get());
        simpleItem(ModItems.UNBREWED_CANNABIS_TEA.get());
        simpleItem(ModItems.CANNABIS_TEA.get());
        simpleItem(ModItems.HASH_MUFFIN.get());
        syringe(ModItems.COCAINE_SYRINGE.get());
        simpleItem(ModItems.OPIUM_BOTTLE_0.get());
        simpleItem(ModItems.OPIUM_BOTTLE_1.get());
        simpleItem(ModItems.OPIUM_BOTTLE_2.get());
        simpleItem(ModItems.OPIUM_BOTTLE_3.get());
        simpleItem(ModItems.MORPHINE_BOTTLE.get());
        syringe(ModItems.MORPHINE_SYRINGE.get());
        simpleItem(ModItems.SOUL_CONCENTRATE.get());
        simpleItem(ModItems.SOUL_RESTER_BOTTLE.get());
        syringe(ModItems.SOUL_RESTER_SYRINGE.get());
        simpleItem(ModItems.SOUL_WRENCHER_BOTTLE.get());
        syringe(ModItems.SOUL_WRENCHER_SYRINGE.get());
        simpleItem(ModItems.CIGARETTE.get());
    }

    protected void simpleItem(Item item) {
        withExistingParent(getItemName(item), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/" + getItemName(item)));
    }

    protected void syringe(Item item) {
        withExistingParent(getItemName(item), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Hallucinocraft.MOD_ID, "item/syringe_overlay"))
                .texture("layer1", new ResourceLocation(Hallucinocraft.MOD_ID, "item/syringe"));
    }

    protected static String getItemName(Item item) {
        return Objects.requireNonNull(item.getRegistryName()).getPath();
    }
}

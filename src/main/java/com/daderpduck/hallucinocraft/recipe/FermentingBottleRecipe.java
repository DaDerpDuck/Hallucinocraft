package com.daderpduck.hallucinocraft.recipe;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.FermentingBottleBlock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FermentingBottleRecipe implements Recipe<FermentingBottleContainer> {
    private final ResourceLocation id;
    private final NonNullList<ItemStack> itemOutputs;
    private final FluidStack fluidOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack recipeFluid;
    private final FermentingBottleBlock.Lid recipeLid;
    private final int duration;

    public FermentingBottleRecipe(ResourceLocation id, NonNullList<ItemStack> itemOutputs, FluidStack fluidOutput, NonNullList<Ingredient> recipeItems, FluidStack recipeFluid, FermentingBottleBlock.Lid recipeLid, int duration) {
        this.id = id;
        this.itemOutputs = itemOutputs;
        this.fluidOutput = fluidOutput;
        this.recipeItems = recipeItems;
        this.recipeFluid = recipeFluid;
        this.recipeLid = recipeLid;
        this.duration = duration;
    }

    @Override
    public boolean matches(FermentingBottleContainer pContainer, Level pLevel) {
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;

        for (int j = 0; j < pContainer.getContainerSize(); j++) {
            ItemStack itemStack = pContainer.getItem(i);
            if (!itemStack.isEmpty()) {
                i++;
                inputs.add(itemStack);
            }
        }

        FluidStack containerFluid = pContainer.getFluid(0);

        return (i == itemOutputs.size()) && (pContainer.getLid() == recipeLid) && (containerFluid.getAmount() >= recipeFluid.getAmount()) && containerFluid.isFluidEqual(recipeFluid) && (RecipeMatcher.findMatches(inputs, recipeItems) != null);
    }

    public ItemStack[] assembleItems() {
        ItemStack[] itemStacks = itemOutputs.toArray(new ItemStack[0]);
        for (int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = itemStacks[i].copy();
        }

        return itemStacks;
    }

    public FluidStack assembleFluid() {
        return fluidOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public FluidStack getRecipeFluid() {
        return recipeFluid;
    }

    public FermentingBottleBlock.Lid getRecipeLid() {
        return recipeLid;
    }

    public NonNullList<ItemStack> getResultItems() {
        return itemOutputs;
    }

    public FluidStack getResultFluid() {
        return fluidOutput;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    @Deprecated
    public ItemStack assemble(FermentingBottleContainer pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public static class Type implements RecipeType<FermentingBottleRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "fermenting_bottle";
    }

    public static class Serializer implements RecipeSerializer<FermentingBottleRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Hallucinocraft.MOD_ID, "fermenting_bottle");

        @Override
        public FermentingBottleRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            // TODO: Check number of input/output items does not exceed what block entity can store
            JsonArray itemOutputsJson = GsonHelper.getAsJsonArray(pSerializedRecipe, "itemResults");
            NonNullList<ItemStack> itemOutputs = NonNullList.withSize(itemOutputsJson.size(), ItemStack.EMPTY);
            for (int i = 0; i < itemOutputsJson.size(); i++) {
                itemOutputs.set(i, ShapedRecipe.itemStackFromJson((JsonObject) itemOutputsJson.get(i)));
            }

            JsonObject fluidOutputJson = GsonHelper.getAsJsonObject(pSerializedRecipe, "fluidResult");
            String fluidOutputName = GsonHelper.getAsString(fluidOutputJson, "fluid");
            int fluidOutputAmount = GsonHelper.getAsInt(fluidOutputJson, "amount");
            FluidStack fluidOutput = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidOutputName))), fluidOutputAmount);

            JsonArray itemInputsJson = GsonHelper.getAsJsonArray(pSerializedRecipe, "itemIngredients");
            NonNullList<Ingredient> itemInputs = NonNullList.withSize(itemInputsJson.size(), Ingredient.EMPTY);
            for (int i = 0; i < itemInputsJson.size(); i++) {
                itemInputs.set(i, Ingredient.fromJson(itemInputsJson.get(i)));
            }

            JsonObject fluidInputJson = GsonHelper.getAsJsonObject(pSerializedRecipe, "fluidInput");
            String fluidInputName = GsonHelper.getAsString(fluidInputJson, "fluid");
            int fluidInputAmount = GsonHelper.getAsInt(fluidInputJson, "amount");
            FluidStack fluidInput = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidInputName))), fluidInputAmount);

            String lidName = GsonHelper.getAsString(pSerializedRecipe, "lid");
            FermentingBottleBlock.Lid lid = switch(lidName) {
                case "cork" -> FermentingBottleBlock.Lid.CORK;
                case "airlock" -> FermentingBottleBlock.Lid.AIRLOCK;
                case "none" -> FermentingBottleBlock.Lid.NONE;
                default -> {
                    Hallucinocraft.LOGGER.warn("Not a valid lid enum");
                    yield FermentingBottleBlock.Lid.NONE;
                }
            };

            int duration = GsonHelper.getAsInt(pSerializedRecipe, "duration");

            return new FermentingBottleRecipe(pRecipeId, itemOutputs, fluidOutput, itemInputs, fluidInput, lid, duration);
        }

        @Nullable
        @Override
        public FermentingBottleRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> itemInputs = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);
            itemInputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));

            FluidStack fluidInput = FluidStack.readFromPacket(pBuffer);

            NonNullList<ItemStack> itemOutputs = NonNullList.withSize(pBuffer.readVarInt(), ItemStack.EMPTY);
            itemOutputs.replaceAll(ignored -> pBuffer.readItem());

            FluidStack fluidOutput = FluidStack.readFromPacket(pBuffer);

            FermentingBottleBlock.Lid lid = pBuffer.readEnum(FermentingBottleBlock.Lid.class);

            int duration = pBuffer.readInt();

            return new FermentingBottleRecipe(pRecipeId, itemOutputs, fluidOutput, itemInputs, fluidInput, lid, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, FermentingBottleRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.getIngredients().size());
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pRecipe.getRecipeFluid().writeToPacket(pBuffer);

            pBuffer.writeVarInt(pRecipe.getResultItems().size());
            for (ItemStack itemStack : pRecipe.getResultItems()) {
                pBuffer.writeItemStack(itemStack, false);
            }

            pRecipe.getResultFluid().writeToPacket(pBuffer);

            pBuffer.writeEnum(pRecipe.getRecipeLid());

            pBuffer.writeInt(pRecipe.getDuration());
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls)
        {
            return (Class<G>)cls;
        }
    }
}

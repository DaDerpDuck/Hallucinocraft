package com.daderpduck.hallucinocraft.recipe;

import com.daderpduck.hallucinocraft.blocks.FermentingBottleBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

// TODO: Flesh this out
public class FermentingBottleContainer extends SimpleContainer {
    private final int fluidSize;
    private final NonNullList<FluidStack> fluids;
    private FermentingBottleBlock.Lid lid;

    public FermentingBottleContainer(int itemSize) {
        super(itemSize);
        this.fluidSize = 0;
        this.fluids = NonNullList.withSize(0, FluidStack.EMPTY);
    }

    public FermentingBottleContainer(ItemStack... items) {
        super(items);
        this.fluidSize = 0;
        this.fluids = NonNullList.withSize(0, FluidStack.EMPTY);
    }

    public FermentingBottleContainer(int itemSize, int fluidSize) {
        super(itemSize);
        this.fluidSize = fluidSize;
        this.fluids = NonNullList.withSize(fluidSize, FluidStack.EMPTY);
    }

    public FermentingBottleContainer(ItemStack[] items, FluidStack[] fluids) {
        super(items);
        this.fluidSize = fluids.length;
        this.fluids = NonNullList.of(FluidStack.EMPTY, fluids);
    }

    public FluidStack getFluid(int index) {
        return index >= 0 && index < fluidSize ? fluids.get(index) : FluidStack.EMPTY;
    }

    public void setFluid(int index, FluidStack stack) {
        fluids.set(index, stack);

        setChanged();
    }

    public void setLid(FermentingBottleBlock.Lid lid) {
        this.lid = lid;
    }

    public FermentingBottleBlock.Lid getLid() {
        return lid;
    }
}

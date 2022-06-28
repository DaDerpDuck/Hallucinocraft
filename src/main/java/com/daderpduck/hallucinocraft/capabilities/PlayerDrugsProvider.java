package com.daderpduck.hallucinocraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDrugsProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<PlayerDrugs> PLAYER_DRUGS = CapabilityManager.get(new CapabilityToken<>(){});

    private PlayerDrugs playerDrugs = null;
    private final LazyOptional<PlayerDrugs> opt = LazyOptional.of(this::createPlayerDrugs);

    @Nonnull
    private PlayerDrugs createPlayerDrugs() {
        if (playerDrugs == null) playerDrugs = new PlayerDrugs();
        return playerDrugs;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return PLAYER_DRUGS.orEmpty(cap, opt);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerDrugs().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerDrugs().loadNBTData(nbt);
    }
}

package com.daderpduck.psychedelicraft.capabilities;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//TODO: Sync capabilities
public class PlayerDrugs {
    static class Implementation implements IPlayerDrugs {
        private final List<DrugInstance> drugs = new ArrayList<>();

        @Override
        public void addDrug(DrugInstance drug) {
            drugs.add(drug);
        }

        @Override
        public void removeDrug(DrugInstance drug) {
            drugs.remove(drug);
        }

        @Override
        public void clearDrugs() {
            drugs.clear();
        }

        @Override
        public List<DrugInstance> getDrugs() {
            return drugs;
        }
    }

    static class Provider implements ICapabilitySerializable<CompoundNBT> {
        private final Implementation defaultImplementation = new Implementation();
        private final LazyOptional<IPlayerDrugs> optional = LazyOptional.of(() -> defaultImplementation);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return optional.cast();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) PlayerProperties.PLAYER_DRUGS.writeNBT(defaultImplementation, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            PlayerProperties.PLAYER_DRUGS.readNBT(defaultImplementation, null, nbt);
        }

        public void invalidate() {
            optional.invalidate();
        }
    }

    static class Storage implements Capability.IStorage<IPlayerDrugs>{
        @Nullable
        @Override
        public INBT writeNBT(Capability<IPlayerDrugs> capability, IPlayerDrugs instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            for (DrugInstance drug : instance.getDrugs()) {
                if (drug.getDesiredEffect() <= 0) continue;
                CompoundNBT drugProperties = new CompoundNBT();
                drugProperties.putInt("delay", drug.getDelayTime());
                drugProperties.putFloat("desire", drug.getDesiredEffect());
                drugProperties.putFloat("current", drug.getCurrentEffect());
                nbt.put(drug.toName(), drugProperties);
            }

            return nbt;
        }

        @Override
        public void readNBT(Capability<IPlayerDrugs> capability, IPlayerDrugs instance, Direction side, INBT nbtIn) {
            if (nbtIn != null) {
                CompoundNBT nbt = (CompoundNBT) nbtIn;

                for (String key : nbt.getAllKeys()) {
                    CompoundNBT drugProperties = nbt.getCompound(key);
                    DrugInstance drugInstance = new DrugInstance(Drug.byName(key), drugProperties.getInt("delay"), drugProperties.getFloat("desire"), drugProperties.getFloat("current"));
                    instance.addDrug(drugInstance);
                }
            }
        }
    }

    static void register() {
        CapabilityManager.INSTANCE.register(IPlayerDrugs.class, new PlayerDrugs.Storage(), PlayerDrugs.Implementation::new);

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, PlayerDrugs::attachCapabilitiesEntity);
    }

    static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            PlayerDrugs.Provider provider = new PlayerDrugs.Provider();
            event.addCapability(new ResourceLocation(Psychedelicraft.MOD_ID, "drugs"), provider);
            event.addListener(provider::invalidate);
        }
    }
}

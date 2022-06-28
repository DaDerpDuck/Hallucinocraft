package com.daderpduck.hallucinocraft.capabilities;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.network.ActiveDrugCapSync;
import com.daderpduck.hallucinocraft.network.DrugCapSync;
import com.daderpduck.hallucinocraft.network.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDrugs {
    private final Map<Drug, Float> active = new HashMap<>();
    private final List<DrugInstance> sources = new ArrayList<>();
    private final Map<Drug, Integer> abuseTimers = new HashMap<>();
    private final DrugEffects drugEffects = new DrugEffects();
    private int smokeTick = 0;

    public static PlayerDrugs getPlayerDrugs(Player player) {
        return player.getCapability(PlayerDrugsProvider.PLAYER_DRUGS).orElse(new PlayerDrugs());
    }

    public void addDrugSource(DrugInstance drug) {
        sources.add(drug);
    }

    public void setSources(List<DrugInstance> drugInstances) {
        sources.clear();
        sources.addAll(drugInstances);
    }

    public void removeDrugSource(DrugInstance drug) {
        sources.remove(drug);
    }

    public void clearDrugSources() {
        sources.clear();
    }

    public List<DrugInstance> getDrugSources() {
        return sources;
    }

    public void putActive(Drug drug, float effect) {
        if (effect > 0) {
            active.put(drug, effect);
        } else {
            active.remove(drug);
        }
    }

    @Nullable
    public Float getActive(Drug drug) {
        return active.get(drug);
    }

    public void clearActives() {
        active.clear();
    }

    public void setActives(Map<Drug, Float> activeDrugs) {
        active.clear();
        active.putAll(activeDrugs);
    }

    public Map<Drug, Float> getActiveDrugs() {
        return active;
    }

    public void addDrugAbuse(Drug drug, int ticks) {
        abuseTimers.put(drug, abuseTimers.getOrDefault(drug, 0) + ticks);
    }

    public int getDrugAbuse(Drug drug) {
        return abuseTimers.getOrDefault(drug, 0);
    }

    public void tickDrugAbuse() {
        abuseTimers.replaceAll((drug, tick) -> tick - 1);
        abuseTimers.values().removeIf(tick -> tick <= 0);
    }

    public void setDrugAbuseMap(Map<Drug, Integer> drugAbuseMap) {
        abuseTimers.clear();
        abuseTimers.putAll(drugAbuseMap);
    }

    public Map<Drug, Integer> getDrugAbuseMap() {
        return abuseTimers;
    }

    public DrugEffects getDrugEffects() {
        return drugEffects;
    }

    public void setSmokeTicks(int ticks) {
        smokeTick = ticks;
    }

    public int getSmokeTicks() {
        return smokeTick;
    }

    public void copyFrom(PlayerDrugs source) {
        active.clear();
        active.putAll(source.active);
        sources.clear();
        sources.addAll(source.sources);
        abuseTimers.clear();
        abuseTimers.putAll(source.abuseTimers);
        smokeTick = source.smokeTick;
    }

    public void saveNBTData(CompoundTag tag) {
        ListTag drugSources = new ListTag();
        for (DrugInstance drugInstance : getDrugSources()) {
            CompoundTag drugProperties = new CompoundTag();
            drugProperties.putString("id", drugInstance.toName());
            drugProperties.putInt("delay", drugInstance.getDelayTime());
            drugProperties.putFloat("potency", drugInstance.getPotency());
            drugProperties.putInt("duration", drugInstance.getDuration());
            drugProperties.putInt("timeActive", drugInstance.getTimeActive());
            drugSources.add(drugProperties);
        }
        tag.put("sources", drugSources);

        CompoundTag drugAbuse = new CompoundTag();
        getDrugAbuseMap().forEach((drug, tick) -> {
            if (tick > 0)
                drugAbuse.putInt(Drug.toName(drug), tick);
        });
        tag.put("abuse", drugAbuse);
    }

    public void loadNBTData(CompoundTag tag) {
        if (tag != null) {
            ListTag drugSources = tag.getList("sources", 10);
            for (Tag drugSource : drugSources) {
                CompoundTag drugProperties = (CompoundTag) drugSource;
                Drug drug = Drug.byName(drugProperties.getString("id"));
                if (drug == null) {
                    Hallucinocraft.LOGGER.warn("Tried to read non-existent registry {} from sources, ignoring", drugProperties.getString("id"));
                    continue;
                }
                DrugInstance drugInstance = new DrugInstance(drug, drugProperties.getInt("delay"), drugProperties.getFloat("potency"), drugProperties.getInt("duration"), drugProperties.getInt("timeActive"));
                addDrugSource(drugInstance);
            }

            CompoundTag drugAbuse = tag.getCompound("abuse");
            for (String drugKey : drugAbuse.getAllKeys()) {
                Drug drug = Drug.byName(drugKey);
                if (drug == null) {
                    Hallucinocraft.LOGGER.warn("Tried to read non-existent registry {} from abuse map, ignoring", drugKey);
                    continue;
                }
                addDrugAbuse(drug, drugAbuse.getInt(drugKey));
            }
        }
    }

    public static void sync(ServerPlayer player) {
        List<DrugInstance> drugInstances = Drug.getDrugSources(player);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new DrugCapSync(drugInstances));
    }

    public static void syncActives(ServerPlayer player) {
        Map<Drug, Float> actives = Drug.getActiveDrugs(player);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ActiveDrugCapSync(actives));
    }
}

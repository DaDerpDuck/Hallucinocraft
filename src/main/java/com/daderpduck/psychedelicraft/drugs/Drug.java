package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.capabilities.IPlayerDrugs;
import com.daderpduck.psychedelicraft.capabilities.PlayerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;

public class Drug extends ForgeRegistryEntry<Drug> {
    private final float maxEffect;
    private final float harmingPoint;
    private final DamageSource damageSource;
    private final Envelope envelope;

    public Drug(DrugProperties properties) {
        this.maxEffect = properties.maxEffect;
        this.harmingPoint = properties.harmingPoint;
        this.damageSource = properties.damageSource;
        this.envelope = Objects.requireNonNull(properties.envelope);
    }

    public static Drug byName(String name) {
        return DrugRegistry.DRUGS.getValue(new ResourceLocation(Psychedelicraft.MOD_ID, name));
    }

    public static String toName(Drug drug) {
        return Objects.requireNonNull(DrugRegistry.DRUGS.getKey(drug)).getPath();
    }

    public static void addDrug(PlayerEntity player, DrugInstance drugInstance) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
        playerDrugs.addDrugSource(drugInstance);
    }

    public static void clearDrugs(PlayerEntity player) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
        playerDrugs.clearDrugSources();
    }

    public static List<DrugInstance> getDrugSources(PlayerEntity player) {
        return PlayerProperties.getPlayerDrugs(player).getDrugSources();
    }

    public static Map<Drug, Float> getActiveDrugs(PlayerEntity player) {
        return PlayerProperties.getPlayerDrugs(player).getActiveDrugs();
    }

    public static void tick(PlayerEntity player) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
        Map<Drug, Float> map = playerDrugs.getActiveDrugs();
        List<DrugInstance> toRemove = new ArrayList<>();

        for (Map.Entry<Drug, Float> entry : map.entrySet()) {
            entry.setValue(entry.getValue()/20F);
        }

        for (DrugInstance drugInstance : playerDrugs.getDrugSources()) {
            Drug drug = drugInstance.getDrug();

            if (drugInstance.isActive()) {
                if (!map.containsKey(drug)) {
                    map.put(drug, 0F);
                }

                float effect = drugInstance.getEffect(drug.getEnvelope());
                if (effect < 0) {
                    toRemove.add(drugInstance);
                    continue;
                }

                map.put(drug, map.get(drug) + effect);
            }
        }

        for (DrugInstance drugInstance : toRemove) {
            playerDrugs.removeDrugSource(drugInstance);
        }

        for (Map.Entry<Drug, Float> entry : map.entrySet()) {
            float clamped = MathHelper.clamp(entry.getValue(), 0, entry.getKey().getMaxEffect());
            if (clamped < 1E-6F) entry.setValue(0F);
            entry.setValue(clamped);
            entry.getKey().effectTick(player, clamped);
        }
    }

    public void effectTick(PlayerEntity player, float effect) {
        if (harmingPoint > 0 && effect < harmingPoint && !player.isInvulnerableTo(damageSource)) {
            player.hurt(damageSource, getDamage(effect));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTick(float effect, float partialTicks) {
    }

    public float getMaxEffect() {
        return maxEffect;
    }

    public float getDamage(float effect) {
        return 0;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public static class DrugProperties {
        private float maxEffect = 5;
        private float harmingPoint = -1;
        private DamageSource damageSource;
        private Envelope envelope;

        public DrugProperties max(float maxEffect) {
            this.maxEffect = maxEffect;
            return this;
        }

        public DrugProperties adsr(float attack, float decay, float sustain, float release) {
            envelope = new Envelope(attack, decay, sustain, release);
            return this;
        }

        public DrugProperties harmfulAt(float harmingPoint, DamageSource damageSource) {
            this.harmingPoint = harmingPoint;
            this.damageSource = damageSource;
            return this;
        }
    }

    public static class Envelope {
        private final float attack;
        private final float decay;
        private final float sustain;
        private final float release;

        public Envelope(float attack, float decay, float sustain, float release) {
            this.attack = attack;
            this.decay = decay;
            this.sustain = sustain;
            this.release = release;
        }

        public float getRisingLevel(float timeRising) {
            if (timeRising < attack) {
                return lerp(0F, 1F, timeRising/attack);
            } else if (timeRising < attack + decay) {
                return lerp(1F, sustain, (timeRising - attack)/decay);
            } else {
                return sustain;
            }
        }

        public float getDecayingLevel(float timeDecaying) {
            return lerp(sustain, 0F, timeDecaying/release);
        }

        private float lerp(float a, float b, float t) {
            return (1F - t)*a + t*b;
        }
    }
}

package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.capabilities.IPlayerDrugs;
import com.daderpduck.psychedelicraft.capabilities.PlayerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Objects;

public class Drug extends ForgeRegistryEntry<Drug> {
    private final float maxEffect;
    private final float harmingPoint;
    private final DamageSource damageSource;

    public Drug(DrugProperties properties) {
        this.maxEffect = properties.maxEffect;
        this.harmingPoint = properties.harmingPoint;
        this.damageSource = properties.damageSource;
    }

    public static Drug byName(String name) {
        return DrugRegistry.DRUGS.getValue(new ResourceLocation(Psychedelicraft.MOD_ID, name));
    }

    public static String toName(Drug drug) {
        return Objects.requireNonNull(DrugRegistry.DRUGS.getKey(drug)).getPath();
    }

    public static void addDrug(PlayerEntity player, DrugInstance drugIn) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
        List<DrugInstance> drugList = playerDrugs.getDrugs();

        if (drugList.contains(drugIn)) {
            DrugInstance playerDrug = drugList.get(drugList.indexOf(drugIn));
            playerDrug.addDesiredEffect(drugIn.getDesiredEffect());
            playerDrug.setDelayTime(Math.min(playerDrug.getDelayTime(), drugIn.getDelayTime()));
        } else {
            playerDrugs.addDrug(drugIn);
        }
    }

    public static void clearDrugs(PlayerEntity player) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);
        playerDrugs.clearDrugs();
    }

    public static List<DrugInstance> getDrugs(PlayerEntity player) {
        return PlayerProperties.getPlayerDrugs(player).getDrugs();
    }

    public static void tick(PlayerEntity player) {
        IPlayerDrugs playerDrugs = PlayerProperties.getPlayerDrugs(player);

        for (DrugInstance drugInstance : playerDrugs.getDrugs()) {
            Drug drug = drugInstance.getDrug();

            if (drug.harmingPoint > 0 && drugInstance.getCurrentEffect() < drug.harmingPoint && !player.isInvulnerableTo(drug.damageSource)) {
                player.hurt(drug.damageSource, drug.getDamage(drugInstance));
            }

            drugInstance.tick();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTick(DrugInstance drugInstance, float partialTicks) {

    }

    public float getMaxEffect() {
        return maxEffect;
    }

    public float getDamage(DrugInstance drugInstance) {
        return 0;
    }

    public static class DrugProperties {
        private float maxEffect = 5;
        private float harmingPoint = -1;
        private DamageSource damageSource = DamageSource.DROWN;

        public DrugProperties max(float maxEffect) {
            this.maxEffect = maxEffect;
            return this;
        }

        public DrugProperties harmfulAt(float harmingPoint) {
            this.harmingPoint = harmingPoint;
            return this;
        }

        public DrugProperties damageSource(DamageSource damageSource) {
            this.damageSource = damageSource;
            return this;
        }
    }
}

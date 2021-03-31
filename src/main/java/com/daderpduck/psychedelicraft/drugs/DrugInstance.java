package com.daderpduck.psychedelicraft.drugs;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DrugInstance {
    private final Drug drug;
    private int delayTime = 0;
    private float currentEffect = 0;
    private float desiredEffect = 0;

    public DrugInstance(Drug drug) {
        this.drug = drug;
    }

    public DrugInstance(Drug drug, int delayTime, float desiredEffect) {
        this.drug = drug;
        this.delayTime = delayTime;
        this.desiredEffect = desiredEffect;
    }

    public DrugInstance(Drug drug, int delayTime, float desiredEffect, float currentEffect) {
        this.drug = drug;
        this.delayTime = delayTime;
        this.currentEffect = currentEffect;
        this.desiredEffect = desiredEffect;
    }

    public String toName() {
        return Drug.toName(drug);
    }

    public Drug getDrug() {
        return drug;
    }

    public void tick() {
        if (delayTime > 0) {
            delayTime--;
        } else {
            this.currentEffect += (desiredEffect - currentEffect)/500F;
            this.currentEffect = MathHelper.clamp(this.currentEffect, 0, 1);
            this.desiredEffect -= desiredEffect/3000F;
            if (this.desiredEffect < 0.1F) this.desiredEffect = 0;
            this.desiredEffect = MathHelper.clamp(desiredEffect, 0, drug.getMaxEffect());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTick(float partialTicks) {

    }

    public int getDelayTime() {
        return delayTime;
    }

    public float getDesiredEffect() {
        return desiredEffect;
    }

    public float getCurrentEffect() {
        return currentEffect;
    }

    public void addDesiredEffect(float modifier) {
        desiredEffect += modifier;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DrugInstance) {
            DrugInstance otherDrug = (DrugInstance) other;
            return otherDrug.drug == this.drug;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return drug.hashCode();
    }
}

package com.daderpduck.psychedelicraft.capabilities;

import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface IPlayerDrugs {
    void addDrugSource(DrugInstance drug);

    void removeDrugSource(DrugInstance drug);

    void setSources(List<DrugInstance> drugInstances);

    void clearDrugSources();

    List<DrugInstance> getDrugSources();

    void putActive(Drug drug, float effect);

    @Nullable
    Float getActive(Drug drug);

    void clearActives();

    void setActives(Map<Drug, Float> activeDrugs);

    Map<Drug, Float> getActiveDrugs();
}

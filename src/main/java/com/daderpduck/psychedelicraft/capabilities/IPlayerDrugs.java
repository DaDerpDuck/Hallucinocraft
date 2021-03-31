package com.daderpduck.psychedelicraft.capabilities;

import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;

import java.util.List;

public interface IPlayerDrugs {
    void addDrug(DrugInstance drug);

    void overrideDrug(DrugInstance drug);

    void setDrugDesiredEffect(Drug drug, float desiredEffect);

    void removeDrug(DrugInstance drug);

    void clearDrugs();

    boolean hasDrug(Drug drug);

    List<DrugInstance> getDrugs();
}

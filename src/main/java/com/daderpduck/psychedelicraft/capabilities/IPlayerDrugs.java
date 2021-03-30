package com.daderpduck.psychedelicraft.capabilities;

import com.daderpduck.psychedelicraft.drugs.DrugInstance;

import java.util.List;

public interface IPlayerDrugs {
    void addDrug(DrugInstance drug);

    void removeDrug(DrugInstance drug);

    void clearDrugs();

    List<DrugInstance> getDrugs();
}

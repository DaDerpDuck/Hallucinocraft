package com.daderpduck.psychedelicraft.capabilities;

import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugInstance;

import java.util.List;

public interface IPlayerDrugs {
    void addDrug(DrugInstance drug);

    void sync(DrugInstance drugInstance);

    void removeDrug(Drug drug);

    void clearDrugs();

    boolean hasDrug(Drug drug);

    List<DrugInstance> getDrugs();
}

package com.pragyaware.anu.solarpack.mModel;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/17/2017.
 */
public class DivisionModel extends RealmObject {

    private String ID, divisionDistrictId, divisionName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDivisionDistrictId() {
        return divisionDistrictId;
    }

    public void setDivisionDistrictId(String divisionDistrictId) {
        this.divisionDistrictId = divisionDistrictId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}

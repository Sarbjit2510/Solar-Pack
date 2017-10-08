package com.pragyaware.anu.solarpack.mModel;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/17/2017.
 */
public class SubDivisionModel extends RealmObject {

    private String ID, subdivisionDivisionId, subdivisionName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSubdivisionDivisionId() {
        return subdivisionDivisionId;
    }

    public void setSubdivisionDivisionId(String subdivisionDivisionId) {
        this.subdivisionDivisionId = subdivisionDivisionId;
    }

    public String getSubdivisionName() {
        return subdivisionName;
    }

    public void setSubdivisionName(String subdivisionName) {
        this.subdivisionName = subdivisionName;
    }
}

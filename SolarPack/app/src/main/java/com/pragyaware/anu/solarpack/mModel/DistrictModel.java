package com.pragyaware.anu.solarpack.mModel;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/17/2017.
 */
public class DistrictModel extends RealmObject {

    private String ID, districtName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}

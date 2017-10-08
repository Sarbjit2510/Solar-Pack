package com.pragyaware.anu.solarpack.mModel;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/17/2017.
 */
public class MutlipleImageModel extends RealmObject {

    private String images;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

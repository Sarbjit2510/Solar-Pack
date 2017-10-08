package com.pragyaware.anu.solarpack.mModel;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sarbjit on 07/05/2017.
 */

public class ViewProjectModel extends RealmObject implements Parcelable {

    private String ID, vendorID, vendorName, vendorMobile, kycType, kycID, projectLocationType, projectType, projectDistrictID, projectDivisionID
            , projectVillage, projectStamp;
    private RealmList<MileStoneModel> mileStoneModels;
    private RealmList<MediaModel> mediaModels;

    private ViewProjectModel(Parcel in) {
        ID = in.readString();
        vendorID = in.readString();
        vendorName = in.readString();
        vendorMobile = in.readString();
        kycType = in.readString();
        kycID = in.readString();
        projectLocationType = in.readString();
        projectType = in.readString();
        projectDistrictID = in.readString();
        projectDivisionID = in.readString();
        projectVillage = in.readString();
        projectStamp = in.readString();
    }

    public ViewProjectModel() {
    }

    public static final Creator<ViewProjectModel> CREATOR = new Creator<ViewProjectModel>() {
        @Override
        public ViewProjectModel createFromParcel(Parcel in) {
            return new ViewProjectModel(in);
        }

        @Override
        public ViewProjectModel[] newArray(int size) {
            return new ViewProjectModel[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorMobile() {
        return vendorMobile;
    }

    public void setVendorMobile(String vendorMobile) {
        this.vendorMobile = vendorMobile;
    }

    public String getKycType() {
        return kycType;
    }

    public void setKycType(String kycType) {
        this.kycType = kycType;
    }

    public String getKycID() {
        return kycID;
    }

    public void setKycID(String kycID) {
        this.kycID = kycID;
    }

    public String getProjectLocationType() {
        return projectLocationType;
    }

    public void setProjectLocationType(String projectLocationType) {
        this.projectLocationType = projectLocationType;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectDistrictID() {
        return projectDistrictID;
    }

    public void setProjectDistrictID(String projectDistrictID) {
        this.projectDistrictID = projectDistrictID;
    }

    public String getProjectDivisionID() {
        return projectDivisionID;
    }

    public void setProjectDivisionID(String projectDivisionID) {
        this.projectDivisionID = projectDivisionID;
    }

    public String getProjectVillage() {
        return projectVillage;
    }

    public void setProjectVillage(String projectVillage) {
        this.projectVillage = projectVillage;
    }

    public String getProjectStamp() {
        return projectStamp;
    }

    public void setProjectStamp(String projectStamp) {
        this.projectStamp = projectStamp;
    }

    public RealmList<MileStoneModel> getMileStoneModels() {
        return mileStoneModels;
    }

    public void setMileStoneModels(RealmList<MileStoneModel> mileStoneModels) {
        this.mileStoneModels = mileStoneModels;
    }

    public RealmList<MediaModel> getMediaModels() {
        return mediaModels;
    }

    public void setMediaModels(RealmList<MediaModel> mediaModels) {
        this.mediaModels = mediaModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(vendorID);
        dest.writeString(vendorName);
        dest.writeString(vendorMobile);
        dest.writeString(kycType);
        dest.writeString(kycID);
        dest.writeString(projectLocationType);
        dest.writeString(projectType);
        dest.writeString(projectDistrictID);
        dest.writeString(projectDivisionID);
        dest.writeString(projectVillage);
        dest.writeString(projectStamp);
    }
}

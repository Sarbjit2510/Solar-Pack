package com.pragyaware.anu.solarpack.mModel;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/15/2017.
 */
public class ProjectModel extends RealmObject implements Parcelable {

    private String ID;
    private String vendorName;
    private String vendorMobile;
    private String kycType;
    private String kycID;
    private String reportStatus;
    private String reportRemarks;
    private String projectDistrictID;
    private String projectDivisionID;
    private String projectSubDivisionID;
    private String reportVillage;
    private String projectMedia;
    private String projectType;
    private String reportStamp;
    private String usrName;
    private String usrMobile;
    private String listType, reportLat, reportLng;


    protected ProjectModel(Parcel in) {
        ID = in.readString();
        vendorName = in.readString();
        vendorMobile = in.readString();
        kycType = in.readString();
        kycID = in.readString();
        reportStatus = in.readString();
        reportRemarks = in.readString();
        projectDistrictID = in.readString();
        projectDivisionID = in.readString();
        projectSubDivisionID = in.readString();
        reportVillage = in.readString();
        projectMedia = in.readString();
        projectType = in.readString();
        reportStamp = in.readString();
        usrName = in.readString();
        usrMobile = in.readString();
        listType = in.readString();
        reportLat = in.readString();
        reportLng = in.readString();
    }

    public ProjectModel() {
    }

    public static final Creator<ProjectModel> CREATOR = new Creator<ProjectModel>() {
        @Override
        public ProjectModel createFromParcel(Parcel in) {
            return new ProjectModel(in);
        }

        @Override
        public ProjectModel[] newArray(int size) {
            return new ProjectModel[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrMobile() {
        return usrMobile;
    }

    public void setUsrMobile(String usrMobile) {
        this.usrMobile = usrMobile;
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

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportRemarks() {
        return reportRemarks;
    }

    public void setReportRemarks(String reportRemarks) {
        this.reportRemarks = reportRemarks;
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

    public String getProjectSubDivisionID() {
        return projectSubDivisionID;
    }

    public void setProjectSubDivisionID(String projectSubDivisionID) {
        this.projectSubDivisionID = projectSubDivisionID;
    }

    public String getReportVillage() {
        return reportVillage;
    }

    public void setReportVillage(String reportVillage) {
        this.reportVillage = reportVillage;
    }

    public String getProjectMedia() {
        return projectMedia;
    }

    public void setProjectMedia(String projectMedia) {
        this.projectMedia = projectMedia;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getReportStamp() {
        return reportStamp;
    }

    public void setReportStamp(String reportStamp) {
        this.reportStamp = reportStamp;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getReportLat() {
        return reportLat;
    }

    public void setReportLat(String reportLat) {
        this.reportLat = reportLat;
    }

    public String getReportLng() {
        return reportLng;
    }

    public void setReportLng(String reportLng) {
        this.reportLng = reportLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(vendorName);
        parcel.writeString(vendorMobile);
        parcel.writeString(kycType);
        parcel.writeString(kycID);
        parcel.writeString(reportStatus);
        parcel.writeString(reportRemarks);
        parcel.writeString(projectDistrictID);
        parcel.writeString(projectDivisionID);
        parcel.writeString(projectSubDivisionID);
        parcel.writeString(reportVillage);
        parcel.writeString(projectMedia);
        parcel.writeString(projectType);
        parcel.writeString(reportStamp);
        parcel.writeString(usrName);
        parcel.writeString(usrMobile);
        parcel.writeString(listType);
        parcel.writeString(reportLat);
        parcel.writeString(reportLng);
    }
}

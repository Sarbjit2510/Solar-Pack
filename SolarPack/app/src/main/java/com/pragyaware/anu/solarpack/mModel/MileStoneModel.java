package com.pragyaware.anu.solarpack.mModel;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 07/05/2017.
 */

public class MileStoneModel extends RealmObject implements Parcelable {

    private String ID, projectID, usrName, reportERemarks, reportLat, reportLng, reportMileStone, reportEStatus, reportStamp, reportARemarks;


    private MileStoneModel(Parcel in) {
        ID = in.readString();
        projectID = in.readString();
        usrName = in.readString();
        reportERemarks = in.readString();
        reportLat = in.readString();
        reportLng = in.readString();
        reportMileStone = in.readString();
        reportEStatus = in.readString();
        reportStamp = in.readString();
        reportARemarks = in.readString();
    }

    public MileStoneModel() {
    }

    public static final Creator<MileStoneModel> CREATOR = new Creator<MileStoneModel>() {
        @Override
        public MileStoneModel createFromParcel(Parcel in) {
            return new MileStoneModel(in);
        }

        @Override
        public MileStoneModel[] newArray(int size) {
            return new MileStoneModel[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getReportERemarks() {
        return reportERemarks;
    }

    public void setReportERemarks(String reportERemarks) {
        this.reportERemarks = reportERemarks;
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

    public String getReportMileStone() {
        return reportMileStone;
    }

    public void setReportMileStone(String reportMileStone) {
        this.reportMileStone = reportMileStone;
    }

    public String getReportEStatus() {
        return reportEStatus;
    }

    public void setReportEStatus(String reportEStatus) {
        this.reportEStatus = reportEStatus;
    }

    public String getReportStamp() {
        return reportStamp;
    }

    public void setReportStamp(String reportStamp) {
        this.reportStamp = reportStamp;
    }

    public String getReportARemarks() {
        return reportARemarks;
    }

    public void setReportARemarks(String reportARemarks) {
        this.reportARemarks = reportARemarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(projectID);
        dest.writeString(usrName);
        dest.writeString(reportERemarks);
        dest.writeString(reportLat);
        dest.writeString(reportLng);
        dest.writeString(reportMileStone);
        dest.writeString(reportEStatus);
        dest.writeString(reportStamp);
        dest.writeString(reportARemarks);
    }
}

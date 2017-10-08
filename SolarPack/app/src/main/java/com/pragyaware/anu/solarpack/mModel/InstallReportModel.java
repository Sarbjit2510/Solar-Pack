package com.pragyaware.anu.solarpack.mModel;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/07/2017.
 */
public class InstallReportModel extends RealmObject {

    private String category, name, kyc_type, kyc_id, kyc_photo, district, subDistt, village, division,
            mobile_no, status, remark, projectId, projectStatus, locationType, BeneficiaryID, Milestone;
    private RealmList<MutlipleImageModel> photoEquipment;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKyc_type() {
        return kyc_type;
    }

    public void setKyc_type(String kyc_type) {
        this.kyc_type = kyc_type;
    }

    public String getKyc_id() {
        return kyc_id;
    }

    public void setKyc_id(String kyc_id) {
        this.kyc_id = kyc_id;
    }

    public String getKyc_photo() {
        return kyc_photo;
    }

    public void setKyc_photo(String kyc_photo) {
        this.kyc_photo = kyc_photo;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubDistt() {
        return subDistt;
    }

    public void setSubDistt(String subDistt) {
        this.subDistt = subDistt;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public RealmList<MutlipleImageModel> getPhotoEquipment() {
        return photoEquipment;
    }

    public String setPhotoEquipment(RealmList<MutlipleImageModel> photoEquipment) {
        this.photoEquipment = photoEquipment;
        return null;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getBeneficiaryID() {
        return BeneficiaryID;
    }

    public void setBeneficiaryID(String beneficiaryID) {
        BeneficiaryID = beneficiaryID;
    }

    public String getMilestone() {
        return Milestone;
    }

    public void setMilestone(String milestone) {
        Milestone = milestone;
    }
}

package com.pragyaware.anu.solarpack.mUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by sarbjit on 05/13/2017.
 */
public class Constants {
    public static final String API_URL = "http://recpdcl.pragyaware.com/WS/i/";
    public static final String IMG_URL = "http://recpdcl.pragyaware.com/i/getIMGBINP?q=";
    public static final String REGISTER = "ProjectOfficerRegistration";
    public static final String LOGIN = "ProjectOfficerLogin";
    public static final String PROJECT_LIST = "ProjectList?";
    public static final String PROJECT_REPORT = "ProjectEngineerUpdate";
    public static final String UPDATE_PROJECT = "ProjectStatusUpdate";
    public static final String PROJECT_MEDIA = "ProjectMedia";
    public static final String APPROVED_LIST = "ProjectListUpdated?";
    public static final String CHANGE_PASSWORD = "OfficerChangePass";
    public static final String APPROVAL_STATUS = "ProjectApproverUpdate";
    public static final String APPROVAL_LIST = "ProjectApproverList";
    public static final String DISAPPROVAL_LIST = "ProjectDList";
    public static final String APPROVE_LIST = "ProjectEngineerList";
    public static final String GETDISTRICT = "GetDistrict?";
    public static final String GET_DIVIDION = "GetDivision?";
    public static final String GET_SUB_DIVISION = "GetSubDivision?";
    public static final String PROJECT_SAVE = "ProjectCreate";
    public static final String PROJECT_NEW_UPDATE = "ProjectUpdate";
    public static final String PROJECT_UPDATE = "ProjectStatusUpdate";
    public static final String PROJECT_MILESTONE = "ProjectMilestone";
    public static final String PROJECT_STATUS_UPDATE = "ProjectReportUpdate";
    public static final String DELETE_MEDIA = "ProjectMediaRemove";

    public static final String[] items = {"Select", "M1-Award of Contract", "M2-Site Survey", "M3-Completion of Civil Work", "M4-Mobilization of Material",
            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};

    public static AsyncHttpClient getClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(20000);
        client.setTimeout(40000);
        client.setMaxRetriesAndTimeout(1, 5000);
        return client;
    }

    public static SyncHttpClient getSyncClient() {
        SyncHttpClient client = new SyncHttpClient();
        client.setConnectTimeout(20000);
        client.setTimeout(40000);
        client.setMaxRetriesAndTimeout(1, 5000);
        return client;
    }

}

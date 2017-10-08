package com.pragyaware.anu.solarpack.mActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.EnggProjectAdaptar;
import com.pragyaware.anu.solarpack.mModel.EnggMilestoneModel;
import com.pragyaware.anu.solarpack.mModel.EnggViewModel;
import com.pragyaware.anu.solarpack.mModel.MediaModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ApprovedActivity extends AppCompatActivity {

    private RecyclerView reportRecyclerVw;
    private Context context;
    private ArrayList<EnggViewModel> projectModels;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        reportRecyclerVw = (RecyclerView) findViewById(R.id.reportRecyclerVw);
        realm = Realm.getDefaultInstance();
        context = this;
        projectModels = new ArrayList<>();
        reportRecyclerVw.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckInternetUtil.isConnected(this)) {
            getProjectList();
        } else {
            RealmResults<EnggViewModel> realmResults = realm.where(EnggViewModel.class).findAll();
            projectModels.addAll(realmResults);
            reportRecyclerVw.setAdapter(new EnggProjectAdaptar(context, projectModels));
        }
    }

    private void getProjectList() {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        String Url = Constants.API_URL + Constants.APPROVE_LIST + "?officerEID=" + PreferencesUtil.getInstance(this).getUserContactId();

        Constants.getClient().get(context, Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();

                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Rows");
                        projectModels = new ArrayList<>();
                        final RealmResults<EnggViewModel> realmList = realm.where(EnggViewModel.class).findAll();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realmList.deleteAllFromRealm();
                            }
                        });
                        try {
                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                EnggViewModel model = new EnggViewModel();
                                model.setID(object.getString("ID"));
                                model.setVendorID(object.getString("vendorID"));
                                model.setVendorName(object.getString("vendorName"));
                                model.setVendorMobile(object.getString("vendorMobile"));
                                model.setKycType(object.getString("kycType"));
                                model.setKycID(object.getString("kycID"));
                                model.setProjectLocationType(object.getString("projectLocationType"));
                                model.setProjectType(object.getString("projectType"));
                                model.setProjectDistrictID(object.getString("projectDistrictID"));
                                model.setProjectDivisionID(object.getString("projectDivisionID"));
                                model.setProjectVillage(object.getString("projectVillage"));
                                model.setProjectStamp(object.getString("projectStamp"));
                                JSONObject jsonObject1 = XML.toJSONObject(object.getString("projectMiles"));
                                JSONObject object1 = jsonObject1.getJSONObject("Miles");
                                JSONArray array = object1.getJSONArray("MileStone");
                                RealmList<EnggMilestoneModel> models = new RealmList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object2 = array.getJSONObject(j);
                                    if (j != array.length() - 1) {
                                        EnggMilestoneModel mileStoneModel = new EnggMilestoneModel();
                                        mileStoneModel.setID(object2.getString("ID"));
                                        mileStoneModel.setProjectID(object2.getString("projectID"));
                                        mileStoneModel.setUsrName(object2.getString("eName"));
                                        mileStoneModel.setaName(object2.optString("aName"));
                                        mileStoneModel.setReportERemarks(object2.optString("reportERemarks"));
                                        mileStoneModel.setReportARemarks(object2.optString("reportARemarks"));
                                        mileStoneModel.setReportLat(object2.getString("Lat"));
                                        mileStoneModel.setReportLng(object2.getString("Lng"));
                                        mileStoneModel.setReportMileStone(object2.getString("MileStone"));
                                        mileStoneModel.setReportEStatus(object2.getString("EStatus"));
                                        mileStoneModel.setReportAStatus(object2.getString("AStatus"));
//                                        mileStoneModel.setReportStamp(object2.getString("reportStamp"));
                                        models.add(mileStoneModel);
                                    }
                                }
                                model.setModels(models);
                                JSONObject object2 = XML.toJSONObject(object.getString("projectMedia"));
                                JSONObject object3 = object2.optJSONObject("Reel");
                                JSONArray array1 = object3.optJSONArray("Media");
                                RealmList<MediaModel> mediaModels = new RealmList<>();
                                for (int j = 0; j < array1.length(); j++) {
                                    JSONObject object4 = array1.getJSONObject(j);
                                    MediaModel mediaModel = new MediaModel();
                                    mediaModel.setID(object4.getString("ProjectMediaID"));
                                    mediaModel.setMediaType(object4.getString("mediaType"));
                                    mediaModels.add(mediaModel);
                                }
                                model.setMediaModels(mediaModels);
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                        }
                        RealmResults<EnggViewModel> realmResults = realm.where(EnggViewModel.class).findAll();
                        projectModels.addAll(realmResults);
                        if (projectModels.size() != 0) {
                            reportRecyclerVw.setAdapter(new EnggProjectAdaptar(context, projectModels));
                        } else {
                            DialogUtil.showDialog(null, "No Record Found", (Activity) context);
                        }

                    } else {
                        DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), context);
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                RealmResults<EnggViewModel> realmResults = realm.where(EnggViewModel.class).findAll();
                projectModels.addAll(realmResults);
                reportRecyclerVw.setAdapter(new EnggProjectAdaptar(context, projectModels));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }


}

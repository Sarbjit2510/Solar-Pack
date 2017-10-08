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
import com.pragyaware.anu.solarpack.mAdaptar.ViewProjectAdaptar;
import com.pragyaware.anu.solarpack.mModel.MediaModel;
import com.pragyaware.anu.solarpack.mModel.MileStoneModel;
import com.pragyaware.anu.solarpack.mModel.ViewProjectModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;

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

public class ProjectApprovalActivity extends AppCompatActivity {

    private RecyclerView reportRecyclerVw;
    private Context context;
    private ArrayList<ViewProjectModel> projectModels;
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
            RealmResults<ViewProjectModel> realmResults = realm.where(ViewProjectModel.class).equalTo("identifier", "AppList").findAll();
            projectModels.addAll(realmResults);
            reportRecyclerVw.setAdapter(new ViewProjectAdaptar(context, projectModels));
        }
    }

    private void getProjectList() {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        String Url = Constants.API_URL + Constants.APPROVAL_LIST;

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
                        final RealmResults<ViewProjectModel> realmList = realm.where(ViewProjectModel.class).findAll();

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
                                ViewProjectModel model = new ViewProjectModel();
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
                                RealmList<MileStoneModel> models = new RealmList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object2 = array.getJSONObject(j);
                                    if (j != array.length() - 1) {
                                        MileStoneModel mileStoneModel = new MileStoneModel();
                                        mileStoneModel.setID(object2.getString("ProjectReportID"));
                                        mileStoneModel.setProjectID(object2.getString("projectID"));
                                        mileStoneModel.setUsrName(object2.getString("eName"));
                                        mileStoneModel.setReportERemarks(object2.optString("reportERemarks"));
                                        mileStoneModel.setReportARemarks(object2.getString("reportARemarks"));
                                        mileStoneModel.setReportLat(object2.getString("Lat"));
                                        mileStoneModel.setReportLng(object2.getString("Lng"));
                                        mileStoneModel.setReportMileStone(object2.getString("MileStone"));
                                        mileStoneModel.setReportEStatus(object2.getString("EStatus"));
                                        mileStoneModel.setReportStamp(object2.getString("AStatus"));
                                        models.add(mileStoneModel);
                                    }
                                }
                                JSONObject object2 = XML.toJSONObject(object.getString("projectMedia"));
                                JSONObject object3 = object2.getJSONObject("Reel");
                                JSONArray array1 = object3.getJSONArray("Media");
                                RealmList<MediaModel> mediaModels = new RealmList<>();
                                for (int j = 0; j < array1.length(); j++) {
                                    JSONObject object4 = array1.getJSONObject(j);
                                    MediaModel mediaModel = new MediaModel();
                                    mediaModel.setID(object4.getString("ProjectMediaID"));
                                    mediaModel.setMediaType(object4.getString("mediaType"));
                                    mediaModels.add(mediaModel);
                                }
                                model.setMediaModels(mediaModels);
                                model.setMileStoneModels(models);
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                        }
                        RealmResults<ViewProjectModel> realmResults = realm.where(ViewProjectModel.class).findAll();
                        projectModels.addAll(realmResults);
                        if (projectModels.size() != 0) {
                            reportRecyclerVw.setAdapter(new ViewProjectAdaptar(context, projectModels));
                        } else {
                            DialogUtil.showDialog(null, "No Project Found", (Activity) context);
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
                RealmResults<ViewProjectModel> realmResults = realm.where(ViewProjectModel.class).equalTo("identifier", "AppList").findAll();
                projectModels.addAll(realmResults);
                reportRecyclerVw.setAdapter(new ViewProjectAdaptar(context, projectModels));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }
}

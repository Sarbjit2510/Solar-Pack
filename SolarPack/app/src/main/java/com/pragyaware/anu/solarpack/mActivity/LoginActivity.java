package com.pragyaware.anu.solarpack.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.SubDivisionModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PermissionUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    Button login;
    private EditText ed_emp_code, ed_password;
    TextView register;
    private Context context;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // checkPermissions();
            PermissionUtil permissionUtil = new PermissionUtil(this);
            permissionUtil.checkPermissions();
        }

        context = this;
        ed_emp_code = (EditText) findViewById(R.id.ed_emp_code);
        ed_password = (EditText) findViewById(R.id.ed_password);
        register = (TextView) findViewById(R.id.register);

        login = (Button) findViewById(R.id.login);
        realm = Realm.getDefaultInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    loginApi();
                }
            }
        });


        if (CheckInternetUtil.isConnected(this)) {
            getAllData(this, realm);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    public static void getAllData(Context context, final Realm realm) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Sync Data...");
        dialog.setCancelable(false);
        dialog.show();


        Constants.getClient().get(context, Constants.API_URL + Constants.GETDISTRICT, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("Status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = object1.getJSONArray("Rows");
                        try {
                            final RealmResults<DistrictModel> results = realm.where(DistrictModel.class).findAll();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    results.deleteAllFromRealm();
                                }
                            });
                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                DistrictModel model = new DistrictModel();
                                model.setID(object.getString("districtID"));
                                model.setDistrictName(object.getString("districtName"));
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                        }
                    }


                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });


        Constants.getClient().get(context, Constants.API_URL + Constants.GET_DIVIDION, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("Rows");
                        try {
                            final RealmResults<DivisionModel> results = realm.where(DivisionModel.class).findAll();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    results.deleteAllFromRealm();
                                }
                            });
                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                DivisionModel model = new DivisionModel();
                                model.setID(object.getString("divisionID"));
                                model.setDivisionDistrictId(object.getString("districtID"));
                                model.setDivisionName(object.getString("divisionName"));
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                        }
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });

        Constants.getClient().get(context, Constants.API_URL + Constants.GET_SUB_DIVISION, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("Rows");
                        try {
                            final RealmResults<SubDivisionModel> results = realm.where(SubDivisionModel.class).findAll();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    results.deleteAllFromRealm();
                                }
                            });
                            realm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                SubDivisionModel model = new SubDivisionModel();
                                model.setID(object.getString("subDivisionID"));
                                model.setSubdivisionDivisionId(object.getString("divisionID"));
                                model.setSubdivisionName(object.getString("subDivisionName"));
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                        }
                        dialog.dismiss();
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });


    }

    private void loginApi() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject object = new JSONObject();
            object.put("usrMobile", ed_emp_code.getText().toString());
            object.put("usrPass", ed_password.getText().toString());
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.LOGIN, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();

                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            PreferencesUtil.getInstance(context).setLoggedIn(true);
                            PreferencesUtil.getInstance(context).setUserContactId(jsonObject.getString("OfficerID"));
                            PreferencesUtil.getInstance(context).setUsername(jsonObject.getString("OfficerName"));
                            if (jsonObject.getString("OfficerRole").equalsIgnoreCase("E")) {
                                PreferencesUtil.getInstance(context).setUserType(PreferencesUtil.ENGG);
                            } else {
                                PreferencesUtil.getInstance(context).setUserType(PreferencesUtil.APPROVAL);
                            }
                            finish();

                            if (PreferencesUtil.getInstance(LoginActivity.this).isOfficer()) {
                                startActivity(new Intent(LoginActivity.this, SiteEngineer.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } else {
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), LoginActivity.this);
                        }

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dialog.dismiss();
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private boolean validate() {

        if (ed_emp_code.getText().toString().equalsIgnoreCase("")) {
            ed_emp_code.setError("Please Enter Mobile Number");
            ed_emp_code.requestFocus();
            return false;
        } else if (ed_emp_code.getText().length() != 10) {
            ed_emp_code.setError("Please Enter Valid Mobile Number");
            ed_emp_code.requestFocus();
            return false;
        } else if (ed_password.getText().toString().equalsIgnoreCase("")) {
            ed_password.setError("Please Enter Password");
            ed_password.requestFocus();
            return false;
        } else if (ed_password.getText().length() < 5) {
            ed_password.setError("Please Enter Password Greater Than 4");
            ed_password.requestFocus();
            return false;
        }

        return true;
    }
}

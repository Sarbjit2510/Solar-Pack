package com.pragyaware.anu.solarpack.mActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mModel.InstallReportModel;
import com.pragyaware.anu.solarpack.mModel.MutlipleImageModel;
import com.pragyaware.anu.solarpack.mModel.ProjectModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.PermissionUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class SiteEngineer extends AppCompatActivity {
    private WebView webview;
    private AlertDialog ad;
    private static int count = 0;
    ProjectModel model;
    private ProgressBar pbProcessing;
    LinearLayout installation_report, approve_list;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_engineer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webview = (WebView) findViewById(R.id.webView);
        pbProcessing = (ProgressBar) findViewById(R.id.pbProcessing);
        approve_list = (LinearLayout) findViewById(R.id.approve_list);
        installation_report = (LinearLayout) findViewById(R.id.installation_report);

        model = getIntent().getParcelableExtra("data");
        webview.setFocusable(true);
        webview.setFocusableInTouchMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtil permissionUtil = new PermissionUtil(this);
            permissionUtil.checkPermissions();
        }

        installation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SiteEngineer.this, SubmitInstallationReport.class));
            }
        });

        approve_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiteEngineer.this, ApprovedActivity.class));
            }
        });

    }


    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (PreferencesUtil.getInstance(SiteEngineer.this).isLoggedIn()) {
                    PreferencesUtil.getInstance(getApplicationContext()).setLoggedIn(false);
                    Intent intent = new Intent(SiteEngineer.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ad.dismiss();
            }
        });
        ad = builder.create();
        ad.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openURLTranscript();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(null);

        builder.setMessage("Do you want to Exit App.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }

        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_slideshow) {
            startActivity(new Intent(SiteEngineer.this, ChangePasswordActivity.class));
            return true;
        } else if (id == R.id.nav_manage) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void openURLTranscript() {
        if (CheckInternetUtil.isConnected(getApplicationContext())) {
            pbProcessing.setVisibility(View.VISIBLE);

            webview.setVisibility(View.VISIBLE);
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    pbProcessing.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    pbProcessing.setVisibility(View.GONE);
                }
            });
            webview.loadUrl("http://recpdcl.pragyaware.com/Project/?h=1");
            webview.requestFocus();
            webview.invalidate();
            Realm realm = Realm.getDefaultInstance();
            LoginActivity.getAllData(this, realm);
            /*if (PreferencesUtil.getInstance(this).isOfficer()) {
                new UploadUserComplaints(getApplicationContext()).execute();
            }*/

        } else {
            webview.setVisibility(View.GONE);
            pbProcessing.setVisibility(View.GONE);
        }
    }

    public static class UploadUserComplaints extends AsyncTask<String, Void, String> {

        @SuppressLint("StaticFieldLeak")
        Context con;


        UploadUserComplaints(Context con) {
            this.con = con;
        }

        @Override
        protected String doInBackground(String... strings) {

            Realm realm = Realm.getDefaultInstance();

            RealmResults<InstallReportModel> realmResults = realm.where(InstallReportModel.class).findAll();

            try {
                for (InstallReportModel model : realmResults) {
                    postData(model, realm, con);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void postData(final InstallReportModel reportModel, final Realm realm, Context con) {

            count = 0;
            try {
                JSONObject params = new JSONObject();
                params.put("officerEID", reportModel.getProjectId());
                params.put("vendorName", reportModel.getName());
                params.put("vendorMobile", reportModel.getMobile_no());
                params.put("kycType", reportModel.getKyc_type());
                params.put("kycID", reportModel.getKyc_id());
                params.put("reportStatus", reportModel.getStatus());
                params.put("reportRemarks", reportModel.getRemark());
                params.put("projectType", reportModel.getCategory());
                params.put("projectDistrictID", reportModel.getDistrict());
                params.put("projectDivisionID", reportModel.getDivision());
                params.put("projectSubDivisionID", reportModel.getSubDistt());
                params.put("reportVillage", reportModel.getVillage());

                StringEntity entity = new StringEntity(params.toString());

                String url;
                if (reportModel.getProjectStatus().equalsIgnoreCase("")) {
                    url = Constants.PROJECT_REPORT;
                } else {
                    url = Constants.UPDATE_PROJECT;
                }

                Constants.getSyncClient().post(con, Constants.API_URL + url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                postImageProject(jsonObject.getString("pID"), reportModel.getKyc_photo(), reportModel.getPhotoEquipment(), realm, reportModel);
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }


        private void postImageProject(final String projectId, String kyc_photo, final RealmList<MutlipleImageModel> photoEquipment, final Realm realm, final InstallReportModel reportModel) {

            try {
                JSONObject params = new JSONObject();
                params.put("mediaProjectID", projectId);
                params.put("mediaType", "101");
                params.put("mediaData", kyc_photo);
                StringEntity entity = new StringEntity(params.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.PROJECT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                if (photoEquipment.size() == 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            reportModel.deleteFromRealm();
                                        }
                                    });
                                } else {
                                    for (MutlipleImageModel s : photoEquipment) {
                                        postEquipmentImage(projectId, s, reportModel, realm);
                                    }
                                }
                            }

                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        private void postEquipmentImage(String projectId, MutlipleImageModel s, final InstallReportModel reportModel, final Realm realm) {


            try {
                JSONObject params = new JSONObject();
                params.put("mediaProjectID", projectId);
                params.put("mediaType", "102");
                params.put("mediaData", s.getImages());
                StringEntity entity = new StringEntity(params.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.PROJECT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                count++;
                                if (count == reportModel.getPhotoEquipment().size()) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            reportModel.deleteFromRealm();
                                        }
                                    });
                                }

                            }

                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
}

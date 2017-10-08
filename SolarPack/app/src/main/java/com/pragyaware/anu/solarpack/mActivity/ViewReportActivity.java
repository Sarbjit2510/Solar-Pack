package com.pragyaware.anu.solarpack.mActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.GridAdaptar;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.ProjectModel;
import com.pragyaware.anu.solarpack.mModel.SubDivisionModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;

public class ViewReportActivity extends AppCompatActivity {

    private TextView vendorName;
    private TextView kycType;
    private TextView kycId, milestone;
    private TextView projectStatus, projecttype, user_name, user_mobile, remark, dateTime;
    private LinearLayout project_layout;
    private LinearLayout submit_layout;
    private ImageView kycImgVw, track_btn;
    private GridView photoGrid;
    private Button approveBtn, disapproveBtn;
    private ProjectModel model;
    private String kycUrl = "";
    private ArrayList<String> photo;
    private Realm realm;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_installation_report);
        vendorName = (TextView) findViewById(R.id.vendorName);
        user_name = (TextView) findViewById(R.id.user_name);
        user_mobile = (TextView) findViewById(R.id.user_mobile);
        kycType = (TextView) findViewById(R.id.kycType);
        kycId = (TextView) findViewById(R.id.kycId);
        projecttype = (TextView) findViewById(R.id.projecttype);
        projectStatus = (TextView) findViewById(R.id.projectStatus);
        kycImgVw = (ImageView) findViewById(R.id.kycImgVw);
        project_layout = (LinearLayout) findViewById(R.id.project_layout);
        submit_layout = (LinearLayout) findViewById(R.id.submit_layout);
        photoGrid = (GridView) findViewById(R.id.photoGrid);
        approveBtn = (Button) findViewById(R.id.approveBtn);
        disapproveBtn = (Button) findViewById(R.id.disapproveBtn);
        remark = (TextView) findViewById(R.id.remark);
        dateTime = (TextView) findViewById(R.id.dateTime);
        milestone = (TextView) findViewById(R.id.milestone);
        track_btn = (ImageView) findViewById(R.id.track_btn);

        model = getIntent().getParcelableExtra("data");
        photo = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        vendorName.setText(model.getVendorName());
        kycType.setText(model.getKycType());
        kycId.setText(model.getKycID());
        projecttype.setText(model.getProjectType());
        projectStatus.setText(model.getReportStatus());
        user_name.setText(model.getUsrName());
        user_mobile.setText(model.getUsrMobile());
        remark.setText(model.getReportRemarks());
        dateTime.setText(model.getReportStamp());
        milestone.setText(model.getReportStatus());

        String distt = realm.where(DistrictModel.class).equalTo("ID", model.getProjectDistrictID()).findFirst().getDistrictName();
        String divis = realm.where(DivisionModel.class).equalTo("ID", model.getProjectDivisionID()).findFirst().getDivisionName();
        String subDiv = realm.where(SubDivisionModel.class).equalTo("ID", model.getProjectSubDivisionID()).findFirst().getSubdivisionName();


        if (model.getProjectMedia().contains(",") && !model.getProjectMedia().startsWith(",")) {
            kycUrl = model.getProjectMedia().split(",")[0];
            String imagUrl = Constants.IMG_URL + kycUrl + "|150|100|40";
            Picasso.with(this).load(imagUrl).into(kycImgVw);
            String[] strings = model.getProjectMedia().split(",");
            for (int i = 0; i < strings.length; i++) {
                if (i != 0) {
                    photo.add(strings[i]);
                }
            }
            if (photo.size() != 0) {
                project_layout.setVisibility(View.VISIBLE);
                photoGrid.setAdapter(new GridAdaptar(ViewReportActivity.this, photo));
            } else {
                project_layout.setVisibility(View.GONE);
            }

        }

        if (!PreferencesUtil.getInstance(this).isOfficer()) {
            submit_layout.setVisibility(View.VISIBLE);

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckInternetUtil.isConnected(ViewReportActivity.this))
                        submitApprovalStatus("1");
                    else
                        DialogUtil.showDialogOK("Alert!", "No Internet Connection", ViewReportActivity.this);
                }
            });

            disapproveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckInternetUtil.isConnected(ViewReportActivity.this))
                        submitApprovalStatus("0");
                    else
                        DialogUtil.showDialogOK("Alert!", "No Internet Connection", ViewReportActivity.this);
                }
            });


        } else {
            submit_layout.setVisibility(View.GONE);
        }

        photoGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        kycImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewReportActivity.this, PreviewActivity.class).putExtra("data", kycUrl));
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = photo.get(i);
                startActivity(new Intent(ViewReportActivity.this, PreviewActivity.class).putExtra("data", url));
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });


    }


    private void fetchLocation() {
        String query = "google.navigation:q=" + model.getReportLat() + "," + model.getReportLng();
        Uri uri = Uri.parse(query);
        //     intent.setData(uri);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void submitApprovalStatus(String approved) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", model.getID());
            jsonObject.put("reportStatus", approved);

            StringEntity entity = new StringEntity(jsonObject.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.APPROVAL_STATUS, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();

                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject object = new JSONObject(response);

                        if (object.getString("Status").equalsIgnoreCase("1")) {
                            DialogUtil.showDialog(null, object.getString("Msg"), ViewReportActivity.this);
                        } else {
                            DialogUtil.showDialogOK("Alert!", object.getString("Msg"), ViewReportActivity.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }
}

package com.pragyaware.anu.solarpack.mActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.PhotoRecyclerView;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.MediaModel;
import com.pragyaware.anu.solarpack.mModel.MileStoneModel;
import com.pragyaware.anu.solarpack.mModel.ViewProjectModel;
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

public class ApproverDetailActivity extends AppCompatActivity {

    EditText districtTxtVw, blockTxtVw, villageTxtVw, projectType, locationTxtVw, nameTxtVw, mobileTxtVw, kycType, kycId, dateTime;
    TextView milestoneTxtVw, statusTxtVw, siteEnggTxtVw, remarkTxtVw, track_btn;
    ImageView kycImgVw, leftImgVw, rightImgVw;
    LinearLayout milestoneLayout;
    RecyclerView photoRecyclerView;
    EditText remarkEdtVw;
    Button approveBtn, disapproveBtn;
    ViewProjectModel model;
    Realm realm;
    ArrayList<String> photo;
    ArrayList<MileStoneModel> mileStoneModels;
    ArrayList<MediaModel> mediaModels;
    String[] strings = {"103", "104", "105", "106", "107", "108"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approver_detail);

        districtTxtVw = (EditText) findViewById(R.id.districtTxtVw);
        blockTxtVw = (EditText) findViewById(R.id.blockTxtVw);
        villageTxtVw = (EditText) findViewById(R.id.villageTxtVw);
        projectType = (EditText) findViewById(R.id.projectType);
        locationTxtVw = (EditText) findViewById(R.id.locationTxtVw);
        nameTxtVw = (EditText) findViewById(R.id.nameTxtVw);
        mobileTxtVw = (EditText) findViewById(R.id.mobileTxtVw);
        kycType = (EditText) findViewById(R.id.kycType);
        kycId = (EditText) findViewById(R.id.kycId);
        dateTime = (EditText) findViewById(R.id.dateTime);
        milestoneTxtVw = (TextView) findViewById(R.id.milestoneTxtVw);
        statusTxtVw = (TextView) findViewById(R.id.statusTxtVw);
        siteEnggTxtVw = (TextView) findViewById(R.id.siteEnggTxtVw);
        remarkTxtVw = (TextView) findViewById(R.id.remarkTxtVw);

        track_btn = (TextView) findViewById(R.id.track_btn);
        kycImgVw = (ImageView) findViewById(R.id.kycImgVw);
        leftImgVw = (ImageView) findViewById(R.id.leftImgVw);
        rightImgVw = (ImageView) findViewById(R.id.rightImgVw);
        milestoneLayout = (LinearLayout) findViewById(R.id.milestoneLayout);
        photoRecyclerView = (RecyclerView) findViewById(R.id.photoRecyclerView);
        remarkEdtVw = (EditText) findViewById(R.id.remarkEdtVw);
        approveBtn = (Button) findViewById(R.id.approveBtn);
        disapproveBtn = (Button) findViewById(R.id.disapproveBtn);

        realm = Realm.getDefaultInstance();
        photo = new ArrayList<>();
        mileStoneModels = new ArrayList<>();
        mediaModels = new ArrayList<>();
        model = getIntent().getParcelableExtra("data");
        mileStoneModels = getIntent().getParcelableArrayListExtra("milestone");
        mediaModels = getIntent().getParcelableArrayListExtra("media");

        final LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(linearLayoutManager1);

        leftImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoRecyclerView.getLayoutManager().scrollToPosition(linearLayoutManager1.findFirstVisibleItemPosition() - 1);
            }
        });

        rightImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoRecyclerView.getLayoutManager().scrollToPosition(linearLayoutManager1.findLastVisibleItemPosition() + 1);
            }
        });

        String distt = realm.where(DistrictModel.class).equalTo("ID", model.getProjectDistrictID()).findFirst().getDistrictName();
        String block = realm.where(DivisionModel.class).equalTo("ID", model.getProjectDivisionID()).findFirst().getDivisionName();
        districtTxtVw.setText(distt);
        blockTxtVw.setText(block);
        villageTxtVw.setText(model.getProjectVillage());
        projectType.setText(model.getProjectType());
        locationTxtVw.setText(model.getProjectLocationType());
        nameTxtVw.setText(model.getVendorName());
        mobileTxtVw.setText(model.getVendorMobile());
        kycType.setText(model.getKycType());
        kycId.setText(model.getKycID());
        dateTime.setText(model.getProjectStamp());
        statusTxtVw.setText(mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus());
        siteEnggTxtVw.setText(mileStoneModels.get(mileStoneModels.size() - 1).getUsrName());
        if (mileStoneModels.get(mileStoneModels.size() - 1).getReportERemarks().equalsIgnoreCase("null"))
            remarkTxtVw.setText("");
        else
            remarkTxtVw.setText(mileStoneModels.get(mileStoneModels.size() - 1).getReportERemarks());

        for (String s : Constants.items) {
            if (s.contains(mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone())) {
                milestoneTxtVw.setText(s);
            }
        }

        if (mileStoneModels.size() > 1) {
            for (int i = 0; i < mileStoneModels.size() - 1; i++) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams")
                View v = vi.inflate(R.layout.layout_milestone, null);
                TextView mileStoneTxtVw = (TextView) v.findViewById(R.id.mileStoneTxtVw);
                TextView statusTxtVw = (TextView) v.findViewById(R.id.statusTxtVw);
                TextView enggTxtVw = (TextView) v.findViewById(R.id.enggTxtVw);
                TextView siteRemTxtVw = (TextView) v.findViewById(R.id.siteRemTxtVw);
//                TextView approveTxtVw = (TextView) v.findViewById(R.id.approveTxtVw);
                TextView remarkTxtVw = (TextView) v.findViewById(R.id.remarkTxtVw);
                ImageView leftImgVw = (ImageView) v.findViewById(R.id.leftImgVw);
                ImageView rightImgVw = (ImageView) v.findViewById(R.id.rightImgVw);
                final RecyclerView mileRecyclerVw = (RecyclerView) v.findViewById(R.id.mileRecyclerVw);
                for (String s : Constants.items) {
                    if (s.contains(mileStoneModels.get(i).getReportMileStone())) {
                        mileStoneTxtVw.setText(s);
                        break;
                    }
                }
                statusTxtVw.setText(mileStoneModels.get(i).getReportEStatus());
                enggTxtVw.setText(mileStoneModels.get(i).getUsrName());
                siteRemTxtVw.setText(mileStoneModels.get(i).getReportERemarks());
//                approveTxtVw.setText(mileStoneModels.get(i).getaName());
                remarkTxtVw.setText(mileStoneModels.get(i).getReportARemarks());
                ArrayList<String> image = new ArrayList<>();
                for (int j = 1; j < mediaModels.size(); j++) {
                    if (mediaModels.get(j).getMediaType().equalsIgnoreCase(strings[i])) {
                        image.add(mediaModels.get(j).getID());
                    }
                }

                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mileRecyclerVw.setLayoutManager(linearLayoutManager);
                mileRecyclerVw.setAdapter(new PhotoRecyclerView(this, image));

                leftImgVw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileRecyclerVw.getLayoutManager().scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
                    }
                });

                rightImgVw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileRecyclerVw.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                    }
                });

                milestoneLayout.addView(v);
            }
        }

        String id = "";
        if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M1")) {
            id = "102";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M2")) {
            id = "103";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M3")) {
            id = "104";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M4")) {
            id = "105";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M5")) {
            id = "106";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M6")) {
            id = "107";
        } else if (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone().equalsIgnoreCase("M7")) {
            id = "108";
        }

        for (int i = 0; i < mediaModels.size(); i++) {
            if (mediaModels.get(i).getMediaType().equalsIgnoreCase(id)) {
                photo.add(mediaModels.get(i).getID());
            }
        }

        for (int i = 0; i < mediaModels.size(); i++) {
            if (mediaModels.get(i).getMediaType().equalsIgnoreCase("101")) {
                String imagUrl = Constants.IMG_URL + mediaModels.get(i).getID() + "|150|100|40";
                Picasso.with(this).load(imagUrl).into(kycImgVw);
                break;
            }
        }
        if (photo.size() != 0) {
            photoRecyclerView.setAdapter(new PhotoRecyclerView(ApproverDetailActivity.this, photo));
        }


        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        kycImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ApproverDetailActivity.this, PreviewActivity.class).putExtra("data", mediaModels.get(0).getID()));
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    submitResponse("1");
                }
            }
        });

        disapproveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    submitResponse("0");
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(null);
        builder.setMessage("Do you want to Discard the Changes.");

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void submitResponse(String status) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject object = new JSONObject();
            object.put("ID", mileStoneModels.get(mileStoneModels.size() - 1).getID());
            object.put("officerAID", PreferencesUtil.getInstance(this).getUserContactId());
            object.put("reportARemarks", remarkEdtVw.getText().toString());
            object.put("reportAStatus", status);
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.PROJECT_STATUS_UPDATE, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();

                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            DialogUtil.showDialog(null, jsonObject.getString("Msg"), ApproverDetailActivity.this);
                        } else {
                            DialogUtil.showDialogOK("Alert", jsonObject.getString("Msg"), ApproverDetailActivity.this);
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

        if (remarkEdtVw.getText().toString().equalsIgnoreCase("")) {
            remarkEdtVw.setError("Please Enter a Remarks");
            remarkEdtVw.requestFocus();
            return false;
        }

        return true;
    }

    private void fetchLocation() {
        String query = "google.navigation:q=" + mileStoneModels.get(mileStoneModels.size() - 1).getReportLat() + ","
                + mileStoneModels.get(mileStoneModels.size() - 1).getReportLng();
        Uri uri = Uri.parse(query);
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


}

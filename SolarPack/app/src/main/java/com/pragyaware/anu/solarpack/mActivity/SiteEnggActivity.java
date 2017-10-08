package com.pragyaware.anu.solarpack.mActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.PhotoGridAdaptar;
import com.pragyaware.anu.solarpack.mAdaptar.PhotoRecyclerView;
import com.pragyaware.anu.solarpack.mFragment.StepSecondFragment;
import com.pragyaware.anu.solarpack.mLocation.GPSTracker;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.EnggMilestoneModel;
import com.pragyaware.anu.solarpack.mModel.EnggViewModel;
import com.pragyaware.anu.solarpack.mModel.MediaModel;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;

public class SiteEnggActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    EditText districtTxtVw, blockTxtVw, villageTxtVw, projectType, locationTxtVw, nameTxtVw, mobileTxtVw, kycType, kycId, dateTime;
    TextView track_btn;
    ImageView kycImgVw, captureImgVw;
    LinearLayout milestoneLayout, progress;
    TextView mileStone_spinner;
    EditText remarkEdtVw;
    Button register;
    Realm realm;
    ArrayList<String> photo;
    EnggViewModel model;
    ArrayList<EnggMilestoneModel> mileStoneModels;
    ArrayList<MediaModel> mediaModels;
    RadioGroup statusRadioGrp;
    TextInputLayout tv_remarks;
    Uri photoURI;
    private GridView photoGrid;
    private PhotoGridAdaptar adaptar;
    private ArrayList<Bitmap> bitmaps;
    ArrayList<String> base64;
    private String mImageFileLocation = "";
    private String milestone = "", milestoneStatus = "";
    private double lat, lng;
    GPSTracker gps;
    private int count = 0;
    String[] strings = {"103", "104", "105", "106", "107", "108"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_engg);

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
        mileStone_spinner = (TextView) findViewById(R.id.mileStone_spinner);
        track_btn = (TextView) findViewById(R.id.track_btn);
        kycImgVw = (ImageView) findViewById(R.id.kycImgVw);
        milestoneLayout = (LinearLayout) findViewById(R.id.milestoneLayout);
        remarkEdtVw = (EditText) findViewById(R.id.ed_remarks);
        register = (Button) findViewById(R.id.register);
        statusRadioGrp = (RadioGroup) findViewById(R.id.statusRadioGrp);
        progress = (LinearLayout) findViewById(R.id.progress);
        tv_remarks = (TextInputLayout) findViewById(R.id.tv_remarks);
        captureImgVw = (ImageView) findViewById(R.id.captureImgVw);
        photoGrid = (GridView) findViewById(R.id.photoGrid);

        realm = Realm.getDefaultInstance();
        photo = new ArrayList<>();
        mediaModels = new ArrayList<>();
        mileStoneModels = new ArrayList<>();
        bitmaps = new ArrayList<>();
        base64 = new ArrayList<>();
        model = getIntent().getParcelableExtra("data");
        mileStoneModels = getIntent().getParcelableArrayListExtra("milestone");
        mediaModels = getIntent().getParcelableArrayListExtra("media");

        setSpinner();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.addition);
        bitmaps.add(icon);
        adaptar = new PhotoGridAdaptar(this, bitmaps);
        photoGrid.setAdapter(adaptar);

        if (gps == null) {
            gps = new GPSTracker(this);
            gps.startUsingGPS();
        }
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        }

        captureImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bitmaps.size() - 1) {
                    takePhoto();
                }
            }
        });

        photoGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        DistrictModel districtModel = realm.where(DistrictModel.class).equalTo("ID", model.getProjectDistrictID()).findFirst();
        if (districtModel != null) {
            String distt = districtModel.getDistrictName();
            districtTxtVw.setText(distt);
        } else {
            districtTxtVw.setText("");
        }
        String block = realm.where(DivisionModel.class).equalTo("ID", model.getProjectDivisionID()).findFirst().getDivisionName();

        blockTxtVw.setText(block);
        villageTxtVw.setText(model.getProjectVillage());
        projectType.setText(model.getProjectType());
        locationTxtVw.setText(model.getProjectLocationType());
        nameTxtVw.setText(model.getVendorName());
        mobileTxtVw.setText(model.getVendorMobile().trim());
        kycType.setText(model.getKycType());
        kycId.setText(model.getKycID());
        dateTime.setText(model.getProjectStamp());

        String imagUrl = Constants.IMG_URL + mediaModels.get(0).getID() + "|150|100|40";
        Picasso.with(this).load(imagUrl).into(kycImgVw);

        if (mileStoneModels.size() != 0) {
            for (int i = 0; i < mileStoneModels.size(); i++) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams")
                View v = vi.inflate(R.layout.layout_milestone, null);
                TextView mileStoneTxtVw = (TextView) v.findViewById(R.id.mileStoneTxtVw);
                TextView statusTxtVw = (TextView) v.findViewById(R.id.statusTxtVw);
                TextView enggTxtVw = (TextView) v.findViewById(R.id.enggTxtVw);
                TextView siteRemTxtVw = (TextView) v.findViewById(R.id.siteRemTxtVw);
                TextView approveTxtVw = (TextView) v.findViewById(R.id.approveTxtVw);
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
                approveTxtVw.setText(mileStoneModels.get(i).getaName());
                remarkTxtVw.setText(mileStoneModels.get(i).getReportARemarks());
                photo = new ArrayList<>();
                for (int j = 1; j < mediaModels.size(); j++) {
                    if (mediaModels.get(j).getMediaType().equalsIgnoreCase(strings[i])) {
                        photo.add(mediaModels.get(j).getID());
                    }
                }

                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mileRecyclerVw.setLayoutManager(linearLayoutManager);
                mileRecyclerVw.setAdapter(new PhotoRecyclerView(this, photo));

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


        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        kycImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SiteEnggActivity.this, PreviewActivity.class).putExtra("data", mediaModels.get(0).getID()));
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    updateMileStone();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    if (gps == null) {
                        gps = new GPSTracker(context);
                        gps.startUsingGPS();
                    }
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        if (mGoogleApiClient == null) {
            initGoogleAPIClient();//Init Google API Client
            checkPermissions();
        } else {
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            if (gps == null) {
                                gps = new GPSTracker(SiteEnggActivity.this);
                                gps.startUsingGPS();
                            }

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SiteEnggActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
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


    private boolean validate() {

        if (milestone.equalsIgnoreCase("") || milestone.equalsIgnoreCase("Select")) {
            DialogUtil.showToast("Please Select MileStone", this);
            mileStone_spinner.requestFocus();
            return false;
        } else if (milestoneStatus.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select MileStone Status", this);
            statusRadioGrp.requestFocus();
            return false;
        } else if (tv_remarks.getVisibility() == View.VISIBLE) {
            if (remarkEdtVw.getText().toString().equalsIgnoreCase("")) {
                remarkEdtVw.setError("Please Enter Remarks");
                remarkEdtVw.requestFocus();
                return false;
            }
        }
        if (bitmaps.size() == 1 || base64.size() == 0) {
            DialogUtil.showToast("Please Capture Image of Site", this);
            photoGrid.requestFocus();
            return false;
        }

        if (lat == 0 || lng == 0) {
            if (gps == null) {
                gps = new GPSTracker(this);
                gps.startUsingGPS();
            }
            if (gps.canGetLocation()) {
                lat = gps.getLatitude();
                lng = gps.getLongitude();
            } else {
                initGoogleAPIClient();//Init Google API Client
                checkPermissions();
            }
        }

        return true;
    }

    private void updateMileStone() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        String remark = "";
        if (tv_remarks.getVisibility() == View.VISIBLE) {
            remark = remarkEdtVw.getText().toString();
        }

        try {
            JSONObject params = new JSONObject();
            params.put("projectID", model.getID());
            params.put("officerEID", PreferencesUtil.getInstance(this).getUserContactId());
            params.put("reportERemarks", remark);
            params.put("reportLat", lat);
            params.put("reportLng", lng);
            params.put("reportMileStone", milestone);
            params.put("reportEStatus", milestoneStatus);
            StringEntity entity = new StringEntity(params.toString());
            String URL = Constants.API_URL + Constants.PROJECT_MILESTONE;

            Constants.getClient().post(this, URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            if (base64.size() == 0) {
                                dialog.dismiss();
                                DialogUtil.showDialog(null, "Status Updated Successfully.",
                                        SiteEnggActivity.this);
                            } else {
                                for (String s : base64) {
                                    switch (milestone) {
                                        case "M2":
                                            postEquipmentImage(model.getID(), s, dialog, "103");
                                            break;

                                        case "M3":
                                            postEquipmentImage(model.getID(), s, dialog, "104");
                                            break;

                                        case "M4":
                                            postEquipmentImage(model.getID(), s, dialog, "105");
                                            break;

                                        case "M5":
                                            postEquipmentImage(model.getID(), s, dialog, "106");
                                            break;

                                        case "M6":
                                            postEquipmentImage(model.getID(), s, dialog, "107");
                                            break;

                                        case "M7":
                                            postEquipmentImage(model.getID(), s, dialog, "108");
                                            break;

                                    }
                                }
                            }
                        } else {
                            dialog.dismiss();
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"),
                                    SiteEnggActivity.this);
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

    private void postEquipmentImage(String id, String s, final ProgressDialog dialog, String mediaType) {

        try {
            JSONObject params = new JSONObject();
            params.put("mediaProjectID", id);
            params.put("mediaType", mediaType);
            params.put("mediaData", s);
            StringEntity entity = new StringEntity(params.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.PROJECT_MEDIA,
                    entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                String response = new String(responseBody, "UTF-8");
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                    count++;
                                    if (count == base64.size()) {
                                        dialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SiteEnggActivity.this);
                                        builder.setTitle(null);

                                        builder.setMessage("Status Updated Successfully");

                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }

                                } else {
                                    dialog.dismiss();
                                    DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"),
                                            SiteEnggActivity.this);
                                }

                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                              Throwable error) {
                            dialog.dismiss();
                        }
                    });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void takePhoto() {

        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String authorities = getPackageName() + ".fileprovider";
        photoURI = FileProvider.getUriForFile(this, authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(callCameraApplicationIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(callCameraApplicationIntent, StepSecondFragment.ACTIVITY_START_CAMERA_APP);
    }

    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
//        imageList.add(mImageFileLocation);

        return image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StepSecondFragment.ACTIVITY_START_CAMERA_APP) {
            if (resultCode == RESULT_OK) {
                setReducedImageSize();
            } else if (resultCode == RESULT_CANCELED) {
                mImageFileLocation = "";
                // user cancelled Image capture
                Toast.makeText(this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                mImageFileLocation = "";
                // failed to capture image
                Toast.makeText(this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            switch (requestCode) {
                // Check for the integer request code originally supplied to startResolutionForResult().
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case RESULT_OK:
                            Log.e("Settings", "Result OK");
                            if (gps == null) {
                                gps = new GPSTracker(this);
                                gps.startUsingGPS();
                            }
                            if (gps.canGetLocation()) {
                                lat = gps.getLatitude();
                                lng = gps.getLongitude();
                            }
                            break;
                        case RESULT_CANCELED:
                            Log.e("Settings", "Result Cancel");
                            mGoogleApiClient.stopAutoManage(this);
                            mGoogleApiClient.disconnect();
                            break;
                    }
                    break;
            }
        }
    }

    private void setReducedImageSize() {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);

        bmOptions.inSampleSize = 8;
        bmOptions.inJustDecodeBounds = false;
        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        bitmaps.add(0, photoReducedSizeBitmp);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoReducedSizeBitmp.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
        byte[] ba = baos.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        base64.add(ba1);
        adaptar.notifyDataSetChanged();
    }

    private void setSpinner() {
        final String[] items = {"Select", "M1", "M2", "M3", "M4", "M5", "M6", "M7"};
        final ArrayList<String> mileStoneStatus = new ArrayList<>();
        switch (mileStoneModels.get(mileStoneModels.size() - 1).getReportMileStone()) {
            case "M1":
                mileStoneStatus.addAll(Arrays.asList(Constants.items).subList(mileStoneModels.size() + 1, Constants.items.length));
                mileStoneStatus.add(0, "Select");
                break;

            case "M2":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M2-Site Survey", "M3-Completion of Civil Work", "M4-Mobilization of Material",
                            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                } else {
                    String[] strings = {"Select", "M3-Completion of Civil Work", "M4-Mobilization of Material",
                            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;
            case "M3":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M3-Completion of Civil Work", "M4-Mobilization of Material",
                            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                } else {
                    String[] strings = {"Select", "M4-Mobilization of Material",
                            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;
            case "M4":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M4-Mobilization of Material",
                            "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                } else {
                    String[] strings = {"Select", "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;
            case "M5":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M5-Installation and fixing of Solar Pack", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                } else {
                    String[] strings = {"Select", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;
            case "M6":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M6-Installation of Sign Board", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                } else {
                    String[] strings = {"Select", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;
            case "M7":
                if (mileStoneModels.get(mileStoneModels.size() - 1).getReportEStatus().equalsIgnoreCase("In Progress")) {
                    String[] strings = {"Select", "M7-Handover"};
                    mileStoneStatus.addAll(Arrays.asList(strings));
                    mileStone_spinner.setText(strings[1]);
                }
                break;


        }

        milestone = "";
        for (String s : items) {
            if (mileStone_spinner.getText().toString().contains(s)) {
                milestone = s;
                break;
            }
        }

      /*  mileStone_spinner.setAdapter(new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.spinnerTxtVw, mileStoneStatus));
        mileStone_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (String s : items) {
                    if (mileStoneStatus.get(position).contains(s)) {
                        milestone = s;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        statusRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.progressRadio == checkedId) {
//                    tv_remarks.setVisibility(View.VISIBLE);
                    milestoneStatus = "In Progress";
                } else if (R.id.completeRadio == checkedId) {
//                    tv_remarks.setVisibility(View.GONE);
                    milestoneStatus = "Completed";
                } else {
//                    tv_remarks.setVisibility(View.GONE);
                    milestoneStatus = "";
                }
            }
        });

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogUtil.showGPSDisabledAlertToUser(this);
    }
}

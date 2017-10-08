package com.pragyaware.anu.solarpack.mFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mActivity.SubmitInstallationReport;
import com.pragyaware.anu.solarpack.mAdaptar.PhotoGridAdaptar;
import com.pragyaware.anu.solarpack.mLocation.GPSTracker;
import com.pragyaware.anu.solarpack.mModel.InstallReportModel;
import com.pragyaware.anu.solarpack.mModel.MutlipleImageModel;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSecondFragment extends Fragment implements BlockingStep, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    Button register;
    ImageView captureImgVw1;
    Uri photoURI;
    private String mImageFileLocation = "", status = "";
    private RealmList<MutlipleImageModel> imageBase;
    private GridView photoGrid1;
    public static final int ACTIVITY_START_CAMERA_APP = 0;
    private ArrayList<Bitmap> bitmaps;
    private PhotoGridAdaptar adaptar;
    private int count = 0;
    private Realm realm;
    LinearLayout progress;
    GPSTracker gps;
    private double lat, lng;
    String address, city, state;
    EditText ed_remarks;
    RadioGroup statusRadioGrp;
    private TextView kyc_status;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_four, container, false);

        kyc_status = (TextView) view.findViewById(R.id.kyc_status);
        register = (Button) view.findViewById(R.id.register);
        captureImgVw1 = (ImageView) view.findViewById(R.id.captureImgVw1);
        photoGrid1 = (GridView) view.findViewById(R.id.photoGrid1);
        progress = (LinearLayout) view.findViewById(R.id.progress);
        ed_remarks = (EditText) view.findViewById(R.id.ed_remarks);
        statusRadioGrp = (RadioGroup) view.findViewById(R.id.statusRadioGrp);

        realm = Realm.getDefaultInstance();
        bitmaps = new ArrayList<>();
        imageBase = new RealmList<>();
        count = 0;

        statusRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.progressRadio == checkedId) {
                    status = "In Progress";
                } else if (R.id.completeRadio == checkedId) {
                    status = "Completed";
                }
            }
        });

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.addition);
        bitmaps.add(icon);
        adaptar = new PhotoGridAdaptar(getActivity(), bitmaps);
        photoGrid1.setAdapter(adaptar);

        getLatLng(false);

        captureImgVw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        photoGrid1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bitmaps.size() - 1) {
                    takePhoto();
                }
            }
        });

        photoGrid1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String[] items = {"M1", "M2", "M3", "M4", "M5", "M6", "M7"};
                    for (String s : items) {
                        if (kyc_status.getText().toString().contains(s)) {
                            SubmitInstallationReport.installReportModel.setMilestone(s);
                            break;
                        }
                    }
                    SubmitInstallationReport.installReportModel.setStatus(status);
                    SubmitInstallationReport.installReportModel.setPhotoEquipment(imageBase);
                    SubmitInstallationReport.installReportModel.setRemark(ed_remarks.getText().toString());
                    submitData(SubmitInstallationReport.installReportModel, Constants.PROJECT_SAVE);

                }
            }
        });
        setSpinner();

        return view;
    }


    @SuppressLint("SetTextI18n")
    private void setSpinner() {
        kyc_status.setText(Constants.items[2]);
    }


    public void getLatLng(boolean sync) {
        if (lat == 0 || lng == 0) {
            return;
        }
        String location = lat + "," + lng;
        String myUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(location)
                + "&sensor=false";
        AsyncHttpClient httpClient;
        if (sync) {
            httpClient = Constants.getSyncClient();
        } else {
            httpClient = Constants.getClient();
        }
        httpClient.get(myUrl, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    JSONObject jso = new JSONObject(str);
                    JSONArray jsa = jso.getJSONArray("results");
                    JSONObject js2 = jsa.getJSONObject(0);

                    JSONArray jsa1 = js2.getJSONArray("address_components");
                    JSONObject js3 = jsa1.getJSONObject(3);
                    JSONObject js4 = jsa1.getJSONObject(4);

                    address = js2.getString("formatted_address");

                    city = js3.getString("long_name");

                    state = js4.getString("long_name");
                } catch (JSONException jse) {
                    jse.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Location Coordinated Not Found !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void submitData(final InstallReportModel reportModel, String url) {
        if (CheckInternetUtil.isConnected(getActivity())) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();
            try {
                JSONObject params = new JSONObject();
                params.put("officerID", PreferencesUtil.getInstance(getActivity()).getUserContactId());
                params.put("vendorName", reportModel.getName());
                params.put("vendorMobile", reportModel.getMobile_no());
                params.put("kycType", reportModel.getKyc_type());
                params.put("kycID", reportModel.getKyc_id());
                params.put("projectType", reportModel.getCategory());
                params.put("projectDistrictID", reportModel.getDistrict());
                params.put("projectDivisionID", reportModel.getDivision());
                params.put("projectVillage", reportModel.getVillage());
                params.put("vendorID", reportModel.getBeneficiaryID());
                params.put("projectLocationType", reportModel.getLocationType());


                StringEntity entity = new StringEntity(params.toString());

                Constants.getClient().post(getActivity(), Constants.API_URL + url, entity,
                        "application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    String response = new String(responseBody, "UTF-8");
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                        postImageProject(jsonObject.getString("projectID"), reportModel, dialog);
                                    } else {
                                        dialog.dismiss();
                                        DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), getActivity());
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
        } else {
            try {
                realm.beginTransaction();
                realm.insert(SubmitInstallationReport.installReportModel);
            } finally {
                realm.commitTransaction();
                DialogUtil.showDialog(null, "Data Saved Successfully.", getActivity());
            }
        }
    }

    private void postImageProject(final String projectId, final InstallReportModel installReportModel,
                                  final ProgressDialog dialog) {

        try {
            JSONObject params = new JSONObject();
            params.put("mediaProjectID", projectId);
            params.put("mediaType", "101");
            params.put("mediaData", installReportModel.getKyc_photo());
            StringEntity entity = new StringEntity(params.toString());

            Constants.getClient().post(getActivity(), Constants.API_URL + Constants.PROJECT_MEDIA,
                    entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                String response = new String(responseBody, "UTF-8");
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equalsIgnoreCase("1")) {

                                    postMileStone(projectId, installReportModel, dialog);

                                } else {
                                    dialog.dismiss();
                                    DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"),
                                            getActivity());
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

    private void postMileStone(final String projectId, final InstallReportModel installReportModel, final ProgressDialog dialog) {

        try {
            JSONObject params = new JSONObject();
            params.put("projectID", projectId);
            params.put("officerEID", PreferencesUtil.getInstance(getActivity()).getUserContactId());
            params.put("reportERemarks", installReportModel.getRemark());
            params.put("reportLat", lat);
            params.put("reportLng", lng);
            params.put("reportMileStone", installReportModel.getMilestone());
            params.put("reportEStatus", installReportModel.getStatus());
            StringEntity entity = new StringEntity(params.toString());
            String URL = Constants.API_URL + Constants.PROJECT_MILESTONE;

            Constants.getClient().post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            if (installReportModel.getPhotoEquipment().size() == 0) {
                                dialog.dismiss();
                                DialogUtil.showDialog(null, "Status Updated Successfully.",
                                        getActivity());
                            } else {
                                for (MutlipleImageModel s : installReportModel.getPhotoEquipment()) {
                                    postEquipmentImage(projectId, s, dialog);
                                }
                            }
                        } else {
                            dialog.dismiss();
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"),
                                    getActivity());
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

    private void postEquipmentImage(final String projectId, MutlipleImageModel s, final ProgressDialog dialog) {

        try {
            JSONObject params = new JSONObject();
            params.put("mediaProjectID", projectId);
            params.put("mediaType", "103");
            params.put("mediaData", s.getImages());
            StringEntity entity = new StringEntity(params.toString());

            Constants.getClient().post(getActivity(), Constants.API_URL + Constants.PROJECT_MEDIA,
                    entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                String response = new String(responseBody, "UTF-8");
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                    count++;
                                    if (count == imageBase.size()) {
                                        dialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(null);

                                        builder.setMessage("Status Updated Successfully");

                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                getActivity().finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }

                                } else {
                                    dialog.dismiss();
                                    DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"),
                                            getActivity());
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

    private boolean validate() {

        if (status.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select Milestone Status", getActivity());
            return false;
        } else if (ed_remarks.getText().toString().equalsIgnoreCase("")) {
            ed_remarks.setError("Please Enter Remarks");
            ed_remarks.requestFocus();
            return false;
        } else if (progress.getVisibility() == View.VISIBLE) {
            if (bitmaps.size() == 1) {
                DialogUtil.showToast("Please Capture Image of Installed Equipments", getActivity());
                return false;
            }
        }


        if (lat == 0 || lng == 0) {
            if (gps == null) {
                gps = new GPSTracker(getActivity());
                gps.startUsingGPS();
            }
            if (gps.canGetLocation()) {
                lat = gps.getLatitude();
                lng = gps.getLongitude();
                if (lat == 0 || lng == 0) {
                    initGoogleAPIClient();//Init Google API Client
                    checkPermissions();
                    return false;
                }/* else {
                    getLatLng(true);
                }*/
            } else {
                initGoogleAPIClient();//Init Google API Client
                checkPermissions();
                return false;
            }
        }

        return true;
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
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
                                gps = new GPSTracker(getActivity());
                                gps.startUsingGPS();
                            }

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        bitmaps = new ArrayList<>();
        imageBase = new RealmList<>();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.addition);
        bitmaps.add(icon);
        adaptar = new PhotoGridAdaptar(getActivity(), bitmaps);
        photoGrid1.setAdapter(adaptar);
        callback.goToPrevStep();
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
        String authorities = getActivity().getPackageName() + ".fileprovider";
        photoURI = FileProvider.getUriForFile(getActivity(), authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(callCameraApplicationIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
//        imageList.add(mImageFileLocation);

        return image;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ACTIVITY_START_CAMERA_APP:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        setReducedImageSize();
                        break;

                    case Activity.RESULT_CANCELED:
                        mImageFileLocation = "";
                        // user cancelled Image capture
                        Toast.makeText(getActivity(),
                                "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
                break;

            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("Settings", "Result OK");
                        if (gps == null) {
                            gps = new GPSTracker(getActivity());
                            gps.startUsingGPS();
                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        mGoogleApiClient.stopAutoManage(getActivity());
                        mGoogleApiClient.disconnect();
                        break;
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            getActivity().unregisterReceiver(gpsLocationReceiver);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
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
        MutlipleImageModel mutlipleImageModel = new MutlipleImageModel();
        mutlipleImageModel.setImages(ba1);
        imageBase.add(mutlipleImageModel);

        adaptar.notifyDataSetChanged();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogUtil.showGPSDisabledAlertToUser(getActivity());
    }
}

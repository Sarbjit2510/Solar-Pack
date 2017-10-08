package com.pragyaware.anu.solarpack.mFragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mActivity.SubmitInstallationReport;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.InstallReportModel;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFirstFragment extends Fragment implements BlockingStep {

    private EditText ed_namee, ed_kfc_id, ed_benef_no;
    RadioGroup catRadioGrp, location_radioGrp;
    private Spinner kyc_spinner;
    private String projectType, locationType = "";
    private String KYC_TYPE = "", projectId = "", projectStatus = "";
    private ImageView kycImgVw;
    private Uri photoURI;
    private String mImageFileLocation = "";
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private EditText ed_village;
    private Spinner dist_spinner, divsion_spinner/*, subDiv_spinner*/;
    private String distt = "", division = "", subDivision = "";
    private Realm realm;
    private ArrayList<String> disttList;
    private ArrayList<String> divisionList;
    private RealmList<DistrictModel> districtModels;
    private RealmList<DivisionModel> divisionModels;
    private EditText ed_mobile_no;
    RadioButton solarRadio, streetRadio, public_radio, house_radio;
    private LinearLayout location_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ed_village = (EditText) view.findViewById(R.id.ed_village);
        ed_benef_no = (EditText) view.findViewById(R.id.ed_benef_no);
        dist_spinner = (Spinner) view.findViewById(R.id.dist_spinner);
        divsion_spinner = (Spinner) view.findViewById(R.id.divsion_spinner);
        ed_mobile_no = (EditText) view.findViewById(R.id.ed_mobile_no);
        solarRadio = (RadioButton) view.findViewById(R.id.solarRadio);
        streetRadio = (RadioButton) view.findViewById(R.id.streetRadio);
        public_radio = (RadioButton) view.findViewById(R.id.public_radio);
        house_radio = (RadioButton) view.findViewById(R.id.house_radio);
        location_radioGrp = (RadioGroup) view.findViewById(R.id.location_radioGrp);
        location_layout = (LinearLayout) view.findViewById(R.id.location_layout);

        realm = Realm.getDefaultInstance();
        disttList = new ArrayList<>();
        divisionList = new ArrayList<>();
        districtModels = new RealmList<>();
        divisionModels = new RealmList<>();

        setDistrictSpinner();

        ed_namee = (EditText) view.findViewById(R.id.ed_namee);
        ed_kfc_id = (EditText) view.findViewById(R.id.ed_kfc_id);
        catRadioGrp = (RadioGroup) view.findViewById(R.id.catRadioGrp);
        kyc_spinner = (Spinner) view.findViewById(R.id.kyc_spinner);
        kycImgVw = (ImageView) view.findViewById(R.id.kycImgVw);

        kycImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });


        projectId = SubmitInstallationReport.installReportModel.getProjectId();
        projectStatus = SubmitInstallationReport.installReportModel.getProjectStatus();
        setSpinner();

        return view;

    }

    private void setDistrictSpinner() {
        disttList = new ArrayList<>();
        divisionList = new ArrayList<>();
        districtModels = new RealmList<>();
        divisionModels = new RealmList<>();
        RealmResults<DistrictModel> districtModel = realm.where(DistrictModel.class).findAll();
        for (DistrictModel model : districtModel) {
            districtModels.add(model);
            disttList.add(model.getDistrictName());
        }

        dist_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, disttList));
        dist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distt = districtModels.get(i).getID();
                setDivision(distt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setDivision(String distt) {
        divisionList = new ArrayList<>();
        divisionModels = new RealmList<>();
        RealmResults<DivisionModel> realmResults = realm.where(DivisionModel.class).equalTo("divisionDistrictId", distt).findAll();
        for (DivisionModel model : realmResults) {
            divisionModels.add(model);
            divisionList.add(model.getDivisionName());
        }

        divsion_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, divisionList));
        divsion_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                division = divisionModels.get(i).getID();
//                setSubDivsion(division);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        return image;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == Activity.RESULT_OK) {
            setReducedImageSize();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            mImageFileLocation = "";
            // user cancelled Image capture
            Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            mImageFileLocation = "";
            // failed to capture image
            Toast.makeText(getActivity(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setReducedImageSize() {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);

        bmOptions.inSampleSize = 10;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        kycImgVw.setImageBitmap(photoReducedSizeBitmap);

    }

    private void setSpinner() {
        final String[] items = {"Select", "Aadhar Card", "PAN Card", "Driving License", "Ration Card", "Voter Card"};

        KYC_TYPE = "";
        kyc_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, items));
        kyc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                KYC_TYPE = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        catRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.solarRadio == i) {
                    projectType = "solar";
                    locationType = "";
                    location_layout.setVisibility(View.VISIBLE);
                } else if (R.id.streetRadio == i) {
                    projectType = "street";
                    location_layout.setVisibility(View.GONE);
                    locationType = "Public";

                    location_radioGrp.clearCheck();
                }
            }
        });

        location_radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.public_radio == checkedId) {
                    locationType = "Public";
                } else if (R.id.house_radio == checkedId) {
                    locationType = "HouseHold";
                }
            }
        });

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (validate()) {
            SubmitInstallationReport.installReportModel = new InstallReportModel();
            SubmitInstallationReport.installReportModel.setCategory(projectType);
            SubmitInstallationReport.installReportModel.setName(ed_namee.getText().toString());
            SubmitInstallationReport.installReportModel.setKyc_type(KYC_TYPE);
            SubmitInstallationReport.installReportModel.setKyc_id(ed_kfc_id.getText().toString());
            SubmitInstallationReport.installReportModel.setProjectId(projectId);
            SubmitInstallationReport.installReportModel.setProjectStatus(projectStatus);
            SubmitInstallationReport.installReportModel.setDistrict(distt);
            SubmitInstallationReport.installReportModel.setDivision(division);
            SubmitInstallationReport.installReportModel.setSubDistt(subDivision);
            SubmitInstallationReport.installReportModel.setLocationType(locationType);
            SubmitInstallationReport.installReportModel.setBeneficiaryID(ed_benef_no.getText().toString());
            SubmitInstallationReport.installReportModel.setMobile_no(ed_mobile_no.getText().toString());
            SubmitInstallationReport.installReportModel.setVillage(ed_village.getText().toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            Bitmap bitmap = ((BitmapDrawable) kycImgVw.getDrawable()).getBitmap();
//            Bitmap bm = BitmapFactory.decodeFile(photoURI.getPath(), options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
            byte[] ba = baos.toByteArray();
            String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
            SubmitInstallationReport.installReportModel.setKyc_photo(ba1);
            callback.goToNextStep();

        }

    }

    private boolean validate() {
        if (distt.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select Division", getActivity());
            dist_spinner.requestFocus();
            return false;
        } else if (division.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select Block", getActivity());
            divsion_spinner.requestFocus();
            return false;
        } else if (!solarRadio.isChecked() && !streetRadio.isChecked()) {
            DialogUtil.showToast("Please Select Project Type", getActivity());
            return false;
        } else if (locationType.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select Location Type", getActivity());
            return false;
        } else if (ed_namee.getText().toString().equalsIgnoreCase("")) {
            ed_namee.setError("Please Enter Consumer Name");
            ed_namee.requestFocus();
            return false;
        } else if (ed_mobile_no.getText().toString().length() != 0 && ed_mobile_no.getText().toString().length() != 10) {
            ed_mobile_no.setError("Please Enter Valid Mobile No");
            ed_mobile_no.requestFocus();
            return false;
        } else if (KYC_TYPE.equalsIgnoreCase("Select")) {
            kyc_spinner.requestFocus();
            DialogUtil.showToast("Please KYC Type", getActivity());
            return false;
        } else if (ed_kfc_id.getText().toString().equalsIgnoreCase("")) {
            ed_kfc_id.setError("Please Enter KYC Id");
            ed_kfc_id.requestFocus();
            return false;
        } else if (mImageFileLocation.equalsIgnoreCase("") || photoURI == null) {
            DialogUtil.showToast("Please Capture Image of KYC ID", getActivity());
            return false;
        } else if (ed_benef_no.getText().toString().equalsIgnoreCase("")) {
            ed_benef_no.setError("Please Enter Beneficiary ID");
            ed_benef_no.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        mImageFileLocation = "";
        photoURI = null;
        ed_village.setText("");
        callback.goToPrevStep();

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
}

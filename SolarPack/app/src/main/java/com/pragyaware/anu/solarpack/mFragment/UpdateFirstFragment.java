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
import com.pragyaware.anu.solarpack.mActivity.SiteEnggFullUpdate;
import com.pragyaware.anu.solarpack.mModel.DistrictModel;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.InstallReportModel;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.squareup.picasso.Picasso;
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
public class UpdateFirstFragment extends Fragment implements BlockingStep {

    private EditText ed_namee, ed_kfc_id, ed_benef_no;
    RadioGroup catRadioGrp, location_radioGrp;
    private Spinner kyc_spinner;
    private String projectType, locationType = "";
    private String KYC_TYPE = "", projectId = "", projectStatus = "";
    private ImageView kycImgVw;
    private LinearLayout location_layout;
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
    public static InstallReportModel installReportModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

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
        installReportModel = new InstallReportModel();

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

        setSpinner();
        ed_namee.setText(SiteEnggFullUpdate.viewModel.getVendorName());
        ed_benef_no.setText(SiteEnggFullUpdate.viewModel.getVendorID());
        ed_kfc_id.setText(SiteEnggFullUpdate.viewModel.getKycID());
        ed_village.setText(SiteEnggFullUpdate.viewModel.getProjectVillage());
        ed_mobile_no.setText(SiteEnggFullUpdate.viewModel.getVendorMobile());

        String URL = Constants.IMG_URL + SiteEnggFullUpdate.mediaModels.get(0).getID() + "|150|100|40";
        Picasso.with(getActivity()).load(URL).into(kycImgVw);

        return view;
    }

    private void setDistrictSpinner() {
        disttList = new ArrayList<>();
        divisionList = new ArrayList<>();
        districtModels = new RealmList<>();
        divisionModels = new RealmList<>();
        int pos = 0;
        RealmResults<DistrictModel> districtModel = realm.where(DistrictModel.class).findAll();
        for (int i = 0; i < districtModel.size(); i++) {
            districtModels.add(districtModel.get(i));
            disttList.add(districtModel.get(i).getDistrictName());
            if (districtModel.get(i).getID().equalsIgnoreCase(SiteEnggFullUpdate.viewModel.getProjectDistrictID())) {
                pos = i;
            }
        }
        dist_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, disttList));
        dist_spinner.setSelection(pos);
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
        int pos = 0;
        RealmResults<DivisionModel> realmResults = realm.where(DivisionModel.class).equalTo("divisionDistrictId", distt).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            divisionModels.add(realmResults.get(i));
            divisionList.add(realmResults.get(i).getDivisionName());
            if (realmResults.get(i).getID().equalsIgnoreCase(SiteEnggFullUpdate.viewModel.getProjectDivisionID())) {
                pos = i;
            }
        }
        divsion_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, divisionList));
        divsion_spinner.setSelection(pos);
        divsion_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                division = divisionModels.get(i).getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (validate()) {
            installReportModel = new InstallReportModel();
            installReportModel.setCategory(projectType);
            installReportModel.setName(ed_namee.getText().toString());
            installReportModel.setKyc_type(KYC_TYPE);
            installReportModel.setKyc_id(ed_kfc_id.getText().toString());
            installReportModel.setProjectId(projectId);
            installReportModel.setProjectStatus(projectStatus);
            installReportModel.setDistrict(distt);
            installReportModel.setDivision(division);
            installReportModel.setSubDistt(subDivision);
            installReportModel.setLocationType(locationType);
            installReportModel.setBeneficiaryID(ed_benef_no.getText().toString());
            installReportModel.setMobile_no(ed_mobile_no.getText().toString());
            installReportModel.setVillage(ed_village.getText().toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 8;
            Bitmap bitmap = ((BitmapDrawable) kycImgVw.getDrawable()).getBitmap();
//            Bitmap bm = BitmapFactory.decodeFile(photoURI.getPath(), options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
            byte[] ba = baos.toByteArray();
            String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
            installReportModel.setKyc_photo(ba1);
            callback.goToNextStep();
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

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

    private boolean validate() {

        if (distt.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select District", getActivity());
            dist_spinner.requestFocus();
            return false;
        } else if (division.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Select Division", getActivity());
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
        } else if (KYC_TYPE.equalsIgnoreCase("Select")) {
            kyc_spinner.requestFocus();
            DialogUtil.showToast("Please KYC Type", getActivity());
            return false;
        } else if (ed_kfc_id.getText().toString().equalsIgnoreCase("")) {
            ed_kfc_id.setError("Please Enter KYC Id");
            ed_kfc_id.requestFocus();
            return false;
        } else if ((mImageFileLocation.equalsIgnoreCase("") || photoURI == null) &&
                SiteEnggFullUpdate.mediaModels.get(0).getID().equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Capture Image of KYC ID", getActivity());
            return false;
        } else if (ed_benef_no.getText().toString().equalsIgnoreCase("")) {
            ed_benef_no.setError("Please Enter Beneficiary ID");
            ed_benef_no.requestFocus();
            return false;
        }
        return true;
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
        int targetImageViewWidth = kycImgVw.getWidth();
        int targetImageViewHeight = kycImgVw.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        bmOptions.inSampleSize = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        kycImgVw.setImageBitmap(photoReducedSizeBitmap);

    }

    private void setSpinner() {
        final String[] items = {"Select", "Aadhar Card", "PAN Card", "Driving License", "Ration Card", "Voter Card"};

        KYC_TYPE = "";
        kyc_spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, R.id.spinnerTxtVw, items));
        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(SiteEnggFullUpdate.viewModel.getKycType())) {
                kyc_spinner.setSelection(i);
                break;
            }
        }
        kyc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                KYC_TYPE = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (SiteEnggFullUpdate.viewModel.getProjectType().equalsIgnoreCase("solar")) {
            solarRadio.setChecked(true);
            streetRadio.setChecked(false);
            location_layout.setVisibility(View.VISIBLE);
            projectType = "solar";
            locationType = "";
        } else if (SiteEnggFullUpdate.viewModel.getProjectType().equalsIgnoreCase("street")) {
            solarRadio.setChecked(false);
            streetRadio.setChecked(true);
            location_layout.setVisibility(View.GONE);
            projectType = "street";
            locationType = "Public";
        }
        catRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.solarRadio == i) {
                    projectType = "solar";
                    locationType = "";
                    location_layout.setVisibility(View.VISIBLE);
                } else if (R.id.streetRadio == i) {
                    projectType = "street";
                    public_radio.setChecked(false);
                    house_radio.setChecked(false);
                    locationType = "Public";
                    location_layout.setVisibility(View.GONE);
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

        if (location_layout.getVisibility() == View.VISIBLE) {
            if (SiteEnggFullUpdate.viewModel.getProjectLocationType().equalsIgnoreCase("Public")) {
                public_radio.setChecked(true);
                house_radio.setChecked(false);
                locationType = "Public";
            } else {
                public_radio.setChecked(false);
                house_radio.setChecked(true);
                locationType = "HouseHold";
            }
        }

    }


}

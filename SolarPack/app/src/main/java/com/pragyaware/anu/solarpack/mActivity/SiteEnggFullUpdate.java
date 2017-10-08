package com.pragyaware.anu.solarpack.mActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.StepperAdaptar;
import com.pragyaware.anu.solarpack.mModel.EnggMilestoneModel;
import com.pragyaware.anu.solarpack.mModel.EnggViewModel;
import com.pragyaware.anu.solarpack.mModel.MediaModel;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class SiteEnggFullUpdate extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
    public static EnggViewModel viewModel;
    public static ArrayList<EnggMilestoneModel> mileStoneModels;
    public static ArrayList<MediaModel> mediaModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdaptar(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

        viewModel = getIntent().getParcelableExtra("data");
        mileStoneModels = getIntent().getParcelableArrayListExtra("milestone");
        mediaModels = getIntent().getParcelableArrayListExtra("media");
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

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
}

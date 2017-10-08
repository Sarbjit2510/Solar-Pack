package com.pragyaware.anu.solarpack.mActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mAdaptar.MyStepperAdapter;
import com.pragyaware.anu.solarpack.mModel.InstallReportModel;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class SubmitInstallationReport extends AppCompatActivity implements StepperLayout.StepperListener {

    StepperLayout mStepperLayout;
    public static InstallReportModel installReportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);

        installReportModel = new InstallReportModel();
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

    }

    @Override
    public void onCompleted(View completeButton) {
        DialogUtil.showToast("Hello Finishing", this);
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

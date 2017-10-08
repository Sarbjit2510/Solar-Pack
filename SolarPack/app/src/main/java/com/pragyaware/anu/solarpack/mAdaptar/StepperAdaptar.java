package com.pragyaware.anu.solarpack.mAdaptar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mFragment.StepFirstFragment;
import com.pragyaware.anu.solarpack.mFragment.StepSecondFragment;
import com.pragyaware.anu.solarpack.mFragment.UpdateFirstFragment;
import com.pragyaware.anu.solarpack.mFragment.UpdateMileStoneFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by sarbjit on 07/06/2017.
 */

public class StepperAdaptar extends AbstractFragmentStepAdapter {

    private String CURRENT_STEP_POSITION_KEY = "key";

    public StepperAdaptar(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        switch (position) {
            case 0:
                UpdateFirstFragment step = new UpdateFirstFragment();
                step.setArguments(b);
                return step;

            case 1:
                UpdateMileStoneFragment step1 = new UpdateMileStoneFragment();
                step1.setArguments(b);
                return step1;

            default:
                UpdateFirstFragment step4 = new UpdateFirstFragment();
                step4.setArguments(b);
                return step4;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(R.string.app_name) //can be a CharSequence instead
                .create();
    }

}

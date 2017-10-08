package com.pragyaware.anu.solarpack.mActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mUtil.CheckInternetUtil;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.pragyaware.anu.solarpack.mUtil.DialogUtil;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edOld_password, edNew_password, ed_password;
    Button submitButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edOld_password = (EditText) findViewById(R.id.edOld_password);
        edNew_password = (EditText) findViewById(R.id.edNew_password);
        ed_password = (EditText) findViewById(R.id.ed_password);
        submitButton = (Button) findViewById(R.id.submitButton);
        context = this;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetUtil.isConnected(context)) {
                    if (validate()) {
                        submitChangePassword();
                    }
                } else {
                    DialogUtil.showDialogOK("Alert!", "No Internet Connection", context);
                }
            }
        });

    }

    private void submitChangePassword() {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", PreferencesUtil.getInstance(context).getUserContactId());
            jsonObject.put("usrPass", edNew_password.getText().toString());
            jsonObject.put("usrOldPass", edOld_password.getText().toString());
            StringEntity entity = new StringEntity(jsonObject.toString());

            Constants.getClient().post(context, Constants.API_URL + Constants.CHANGE_PASSWORD, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject object1 = new JSONObject(response);

                        if (object1.getString("Status").equalsIgnoreCase("1")) {
                            DialogUtil.showDialog(null, object1.getString("Msg"), (Activity) context);
                        } else {
                            DialogUtil.showDialogOK("Alert!", object1.getString("Msg"), context);
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

        if (edOld_password.getText().toString().equalsIgnoreCase("")) {
            edOld_password.setError("Please Enter Old Password");
            edOld_password.requestFocus();
            return false;
        } else if (edOld_password.getText().length() < 5) {
            edOld_password.setError("Please Enter Old Password Length Greater Than 4");
            edOld_password.requestFocus();
            return false;
        } else if (edNew_password.getText().toString().equalsIgnoreCase("")) {
            edNew_password.setError("Please Enter New Password");
            edNew_password.requestFocus();
            return false;
        } else if (edNew_password.getText().length() < 5) {
            edNew_password.setError("Please Enter New Password Length Greater Than 4");
            edNew_password.requestFocus();
            return false;
        } else if (ed_password.getText().toString().equalsIgnoreCase("")) {
            ed_password.setError("Please Enter Re-type Password");
            ed_password.requestFocus();
            return false;
        } else if (ed_password.getText().length() < 5) {
            ed_password.setError("Please Enter Re-type Password Length Greater Than 4");
            ed_password.requestFocus();
            return false;
        } else if (!edNew_password.getText().toString().equalsIgnoreCase(ed_password.getText().toString())) {
            ed_password.setError("Password Mismatches.");
            ed_password.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }
}

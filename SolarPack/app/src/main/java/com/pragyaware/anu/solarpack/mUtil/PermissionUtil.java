package com.pragyaware.anu.solarpack.mUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C9 on 09/08/16.
 */
public class PermissionUtil {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 61;
    private Context con;

    public PermissionUtil(Activity activity) {
        this.con = activity;
    }

    public boolean checkPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("To Capture Images");

        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write To External Storage");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read From External Storage");

//        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
//            permissionsNeeded.add("Make Calls");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("Detect Network States");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Access GPS Fine Location");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access GPS Coarse Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) con, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return true;
            }
          /*  requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);*/

        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this.con, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!((Activity) con).shouldShowRequestPermissionRationale(permission))
                return false;
        } else if (ContextCompat.checkSelfPermission(this.con, permission) == PackageManager.PERMISSION_DENIED) {
            permissionsList.add(permission);
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(con)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

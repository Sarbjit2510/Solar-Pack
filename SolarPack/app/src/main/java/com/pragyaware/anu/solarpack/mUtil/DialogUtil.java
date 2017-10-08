package com.pragyaware.anu.solarpack.mUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Toast;

import com.pragyaware.anu.solarpack.R;

/**
 * Created by sarbjit on 14-07-2016.
 */
public class DialogUtil {

    public static void showDialogOK(String title, String msg,
                                    Context con) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title);
//        builder.setIcon(R.drawable.ic_action_info);

        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showToast(String msg,
                                 Context con) {
        Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showGPSDisabledAlertToUser(final Context con) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        alertDialogBuilder.setTitle("Info");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
        alertDialogBuilder
                .setMessage("GPS is disabled in your device. Please select USE GPS SATELLITES option form list to enable it!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        con.startActivity(callGPSSettingIntent);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public static void showDialog(String title, String msg,
                                  final Activity con) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title);
//        builder.setIcon(R.drawable.ic_action_info);

        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                con.finish();
                con.overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDialogHome(String title, String msg,
                                      final Activity con) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title);
//        builder.setIcon(R.drawable.ic_action_info);

        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                con.finish();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 45, 45);
        drawable.draw(canvas);

        return bitmap;
    }


}

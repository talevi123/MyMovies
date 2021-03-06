package com.tal.mymovies.Receivers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.tal.mymovies.MyMoviesApplication;


public class GpsLocationReceiver extends BroadcastReceiver {

    public GpsLocationReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            boolean enabled = (boolean) intent.getExtras().get("enabled");
            if (!enabled) {
                showSettingsAlert();
            }
        }
    }

    public static void showSettingsAlert() {

        final Activity currentActivity = MyMoviesApplication.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(currentActivity);
            alertDialog.setTitle("GPS is settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    currentActivity.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }
}
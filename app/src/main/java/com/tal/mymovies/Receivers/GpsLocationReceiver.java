package com.tal.mymovies.Receivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.tal.mymovies.Activities.MoviesListActivity;


public class GpsLocationReceiver extends BroadcastReceiver {

    private Context context;

    public GpsLocationReceiver() {

    }

    public void onReceive(Context context, Intent intent) {
     //   if(intent.getAction().matches("android.location.PROVIDERS_CHANGED")){
     //   }

        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        lm.addGpsStatusListener(new android.location.GpsStatus.Listener(){

           public void onGpsStatusChanged(int event){
              switch (event){
                  case context.LOCATION_SERVICE.GPS_EVENT_STARTED:
                    showSettingsAlert();
                      break;
                  case context.LOCATION_SERVICE.GPS_EVENT_STOPPED:
                      showSettingsAlert();
                      break;
              }
           }
        });
    }

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
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

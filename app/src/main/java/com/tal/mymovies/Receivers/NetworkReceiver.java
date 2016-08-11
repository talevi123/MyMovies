package com.tal.mymovies.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkReceiver";

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Got network change event");
        boolean noConnection = intent.getBooleanExtra("noConnectivity", false);
        NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get("networkInfo");
        if (networkInfo != null && networkInfo.getTypeName().equals("WIFI")) {
            if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                // Stop upload
            }
        }
        if (noConnection) {
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Connection available", Toast.LENGTH_SHORT).show();
        }
    }
}

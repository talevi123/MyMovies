package com.tal.mymovies.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ronen_abraham on 7/7/16.
 */
public class ConnectionManager {

    private static final String TAG = "ConnectionManager";

    public static String sendGetRequest(String url) {
        InputStream is = null;
        String result = null;

        try {
            URL mUrl = new URL(url.replace(" ", "%20"));
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            result = inputStreamToString(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static String inputStreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}

package com.tal.mymovies.Moduls;

import org.json.JSONException;
import org.json.JSONObject;

public class Cinema {
    public String name;
    public String vicinity;
    public String latitude;
    public String longitude;


    public Cinema(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.optString("name");
        this.vicinity = jsonObject.optString("vicinity");
        this.latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
        this.longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
    }

    public String getName(){return name;}

    public String getVicinity(){return vicinity;}

    public String getLatitude(){return latitude;}

    public String getLongitude(){return longitude;}
}
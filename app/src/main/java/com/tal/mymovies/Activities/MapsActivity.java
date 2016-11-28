package com.tal.mymovies.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tal.mymovies.Moduls.Cinema;
import com.tal.mymovies.Network.ApiManager;
import com.tal.mymovies.Network.Utility;
import com.tal.mymovies.R;

import java.util.List;

/**
 * Created by tal on 20/11/16.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap map;
    GoogleApiClient googleApiClient;
    double latitude;
    double longitude;
    Location lastLocation;
    Marker markerCurrentLocation;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Utility.requstlocationPremission(MapsActivity.this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        map = gmap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        if (locationRequest != null) {
            showCloseTheaters();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (markerCurrentLocation != null) {
            markerCurrentLocation.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My location");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        markerCurrentLocation = map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        }

        if (map != null) {
            showCloseTheaters();
        }
    }

    private void showCloseTheaters() {
        String url = ApiManager.getUrl(latitude, longitude);
        new SearchCinema().execute(url);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PREMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void ShowNearbyPlaces(List<Cinema> cinemas) {
        for (int i = 0; i < cinemas.size(); i++) {
            Cinema cinema = cinemas.get(i);
            double lat = Double.parseDouble(cinema.getLatitude());
            double lng = Double.parseDouble(cinema.getLongitude());
            String placeName = cinema.getName();
            String vicinity = cinema.getVicinity();
            LatLng latLng = new LatLng(lat, lng);
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(placeName + " : " + vicinity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    public class SearchCinema extends AsyncTask<String, Void, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(String... strings) {
            return ApiManager.getPlaces(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Cinema> cinemas) {
            ShowNearbyPlaces(cinemas);
        }

    }
}

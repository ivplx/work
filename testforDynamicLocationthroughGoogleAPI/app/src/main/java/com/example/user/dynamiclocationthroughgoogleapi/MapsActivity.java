package com.example.user.dynamiclocationthroughgoogleapi;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Marker lastMarker = null;
    Marker marker= null;
    LatLng latlng = null;
    RequestQueue mQueue = Volley.newRequestQueue(this);
    final String url_to_PHP = "http://140.130.19.38:58136/~s12/connect.php";//雲科大LAB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    private synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override//MapsActivity METHOD
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(24.1367351,120.6850057);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomBy(14));
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("系統服務").setMessage("定位服務尚未開啟，\n" + "請問是否開啟？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("否",null).create().show();
        }else{
            Toast.makeText(this,"服務啟用中",Toast.LENGTH_LONG).show();
        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(, 16))

    }



    //GOOGLE API METHOD
    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    //GOOGLE API METHOD
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"暫停連線到 Google",Toast.LENGTH_SHORT).show();
    }

    private LocationRequest createLocationRequest(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    //LOCATIONLISTENER METHOD
    @Override
    public void onLocationChanged(Location location) {

        latlng = new LatLng(location.getLatitude(),location.getLongitude());

        mapAddMarker(mMap, latlng, "！");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    private void mapAddMarker(GoogleMap map, LatLng latlng,String title){

        if(lastMarker != null) {
            lastMarker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latlng).title(title));
        lastMarker = marker;
    }



}

/*
* NCHU iot course 5th team
*  */

package com.example.user.googlemapapi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,LocationListener ,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Marker lastMarker = null;
    Marker lastClickedMarker = null;
    Marker marker= null;
    Marker parkingPlaceMarker = null;
    Marker lastParkingPlaceMarker = null;
    LatLng latlng = null;
    LatLng targetLatLng = null;
    LatLng defaultLocation;
    RequestQueue mQueue;
    Polyline line = null;

    ToggleButton mapDirectionToggleButton;
    Button button;
    Button button2;
    CheckBox followCheckBox;

    String url_to_PHP = "http://140.130.19.38:58136/~s12/connect.php";//虎科LAB(測試用)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //VOLLEY netRequest
        this.buildGoogleApiClient();
        mGoogleApiClient.connect();
        volleyRequest();


        defaultLocation = new LatLng(24.1367938,120.685012);
        mapDirectionToggleButton = (ToggleButton)findViewById(R.id.directionToggleButton);
        button = (Button)findViewById(R.id.buttonMemory);
        button2 = (Button)findViewById(R.id.buttonDisplay);
        button.setOnClickListener(buttonClickListener);
        button2.setOnClickListener(buttonClickListener);
        followCheckBox = (CheckBox)findViewById(R.id.checkBox);
        //測試能否連接到PHP
        //先取得data伺服器資料
    }

    Button.OnClickListener buttonClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.buttonMemory){
                if(latlng != null) {
                    SharedPreferences locSetting = getSharedPreferences("location", 0);
                    locSetting.edit().putString("locLat", String.valueOf(latlng.latitude)).apply();
                    locSetting.edit().putString("locLng", String.valueOf(latlng.longitude)).apply();
                    Toast.makeText(MapsActivity.this, "已儲存停車資訊", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MapsActivity.this,"目前似乎無法儲存現在位置",Toast.LENGTH_LONG).show();
                }
            }
            if(v.getId() == R.id.buttonDisplay){
                SharedPreferences locSetting = getSharedPreferences("location", 0);
                String strLat = locSetting.getString("locLat", "");
                String strLng = locSetting.getString("locLng","");
                LatLng locLatlng = new LatLng(Double.parseDouble(strLat),Double.parseDouble(strLng));
                //mapAddMarker(mMap,locLatlng,"停車位在這");
                if(lastParkingPlaceMarker != null) {
                    lastParkingPlaceMarker.remove();
                }
                parkingPlaceMarker = mMap.addMarker(new MarkerOptions().position(locLatlng).title("停車位在這").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                lastParkingPlaceMarker = parkingPlaceMarker;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(locLatlng));
            }
        }
    };

    ToggleButton.OnCheckedChangeListener directionCheckListener = new ToggleButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Toast.makeText(MapsActivity.this,"開啟中",Toast.LENGTH_SHORT).show();
            }
        }
    };

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
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        Location lastLoc = null;
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
            try {
                lastLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }catch(SecurityException se){
                Toast.makeText(MapsActivity.this,"無法取得定位資訊",Toast.LENGTH_SHORT).show();
            }
            if(lastLoc != null) {
                LatLng gpsLastLoc = new LatLng(lastLoc.getLatitude(),lastLoc.getLongitude());
                mapAddMarker(mMap,gpsLastLoc,"上次位置");

                Toast.makeText(this,"服務啟用中",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"未能取得上一次定位資訊，定位標記可能較晚出現",Toast.LENGTH_LONG).show();
            }

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        mapDirectionToggleButton.setOnCheckedChangeListener(directionCheckListener);
        mMap.setOnMarkerClickListener(this);
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

    private LocationRequest createLocationRequest(){//google service 參數設定
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    //LOCATIONLISTENER METHOD
    @Override
    public void onLocationChanged(Location location) {
        latlng = new LatLng(location.getLatitude(),location.getLongitude());
        mapAddMarker(mMap, latlng, "！");
        //畫面固定中心為定位點
        if(followCheckBox.isChecked()) {
            moveToMyLocation();
        }

        if(mapDirectionToggleButton.isChecked()) {
            if(line != null){//沒加這個會閃退
                line.setVisible(true);
            }
            if(latlng != null) {
                if(targetLatLng == null) {
                    Toast.makeText(MapsActivity.this, "請先點選停車位", Toast.LENGTH_SHORT).show();
                    mapDirectionToggleButton.setChecked(false);
                }else {
                    googleDirectionRequest(latlng.latitude, latlng.longitude, targetLatLng.latitude, targetLatLng.longitude);//開關打開時才進行google導航
                }
            }else{
                Toast.makeText(MapsActivity.this,"目前尚未取得定位資訊，導航失敗",Toast.LENGTH_LONG).show();
            }
        }
        else{
            if(line != null) {
                line.setVisible(false);
            }
        }
    }
    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    //是只顯示一個點的方法
    private void mapAddMarker(GoogleMap map, LatLng latlng,String title){

        if(lastMarker != null) {
            lastMarker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latlng).title(title));
        lastMarker = marker;
    }

    private void volleyRequest(){
        //使用jsonArray取代jsonObject
        JSONArray jsonArray = new JSONArray();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url_to_PHP, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Double lat = 0.0;
                Double lng = 0.0;
                mMap.clear();
                for(int i = 0; i < response.length(); i++){
                    try {// 處理方式:  JSONARRAY > 用getJSONObject取二維JSON陣列中的一段,再用getString("NAME")取這一段其中的一個元素
                        //必須要先分離出lat和lng才能打點
                        lat = Double.parseDouble(response.getJSONObject(i).getString("lat"));
                        lng = Double.parseDouble(response.getJSONObject(i).getString("lng"));
                        LatLng latLng1 = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this,"OOPS,看起來資料伺服器有點問題",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(jsonArrayRequest);
    }

    void moveToMyLocation(){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    void googleDirectionRequest(double originLat,double originLng,double destinationLat,double destinationLng){
        JSONObject jsonObject = new JSONObject();
        //http://maps.googleapis.com/maps/api/directions/json?origin=24.1771619,120.6182997&destination=24.1367938,120.685012&sensor=false
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://maps.googleapis.com/maps/api/directions/json?origin=" + originLat + "," + originLng + "&destination=" + destinationLat + "," + destinationLng + "&sensor=false", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 必須要初始化
                Polyline lastline = null;
                //google送回來的json object有多層架構，須一層一層拆開(拆到overview_polyline)
                try {
                    JSONObject jsonObject = (JSONObject)response.getJSONArray("routes").get(0);
                    JSONObject overviewPolyline = jsonObject.getJSONObject("overview_polyline");
                    String encodePoint = overviewPolyline.getString("points");
                    List<LatLng> list = decodePoly(encodePoint);
                    if(line != null) {
                        line.remove();
                    }
                    line = mMap.addPolyline(new PolylineOptions().addAll(list).width(12).color(Color.parseColor("#FF0000")).geodesic(true));
                }catch(JSONException e){
                    Toast.makeText(MapsActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this,"google導航資訊擷取失敗",Toast.LENGTH_SHORT).show();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        mQueue.add(jsonObjectRequest);
    }
    //針對 "overview_polyline" 編碼過的資訊進行拆解成latlng格式 :(起點latlng,終點latlng)
    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getPosition().equals(latlng)) {
            Toast.makeText(MapsActivity.this, "非停車位", Toast.LENGTH_SHORT).show();
        }else{
            if (lastClickedMarker != null) {
                lastClickedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            targetLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            lastClickedMarker = marker;
        }
        return true;
    }
}


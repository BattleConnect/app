package com.cs495.battleelite.battleelite;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.fragments.MapFilterFragment;
import com.cs495.battleelite.battleelite.holders.objects.ForceData;
import com.cs495.battleelite.battleelite.holders.objects.ForceMarker;
import com.cs495.battleelite.battleelite.holders.objects.SensorData;
import com.cs495.battleelite.battleelite.holders.objects.SensorMarker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, MapFilterFragment.MapFilterFragmentListener {

    private static final String NONE = "none";
    private GoogleMap mMap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Map<String, Object>> forces; //key = internally used unique ID, object = most recent force data entry

    BiMap<Long, SensorData> sensorDataList = HashBiMap.create();
    BiMap<Long, Marker> sensorMarkerList = HashBiMap.create();
    BiMap<Long, SensorMarker> sensorObjectMarkerList = HashBiMap.create();

    BiMap<String, ForceData> forceDataList = HashBiMap.create();
    BiMap<String, Marker> forceMarkerList = HashBiMap.create();
    BiMap<String, ForceMarker> forceObjectMarkerList = HashBiMap.create();
    Map<Marker, ValueAnimator> animatedSensorList = new HashMap();

    boolean[] sensorFilterIndices;
    boolean[] forceFilterIndices;
    boolean[] otherFilterIndices;

    LatLngBounds.Builder boundsBuilder;
    LatLngBounds bounds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        configureFilterButton();
    }

    private void configureFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFilterFragment filter = MapFilterFragment.newInstance(sensorFilterIndices, forceFilterIndices, otherFilterIndices);
                filter.show(getFragmentManager(), "MapFilterFragment");

            }

        });

    }

    @Override
    public void getSelectedForceTypeFilter(List<String> filters) {
        //Log.i("getSelectedSensorTypes", "returns " + type);
        filterForces(filters);
    }

    void filterForces(List<String> forceFilter) {
        for (Map.Entry<String, ForceMarker> entry : forceObjectMarkerList.entrySet()) {
            ForceMarker forceMarker = entry.getValue();

            if(forceFilter.contains(forceMarker.getType())) {
                forceMarker.getMarker().setVisible(true);
            }
            else if(forceFilter.contains(NONE)) {
                forceMarker.getMarker().setVisible(true);
            }
            else if(!forceFilter.contains(forceMarker.getType())) {
                forceMarker.getMarker().setVisible(false);
            }

            if(forceFilter.size() == 0) {
                forceMarker.getMarker().setVisible(true);
            }
        }
    }

    @Override
    public void getSelectedSensorTypeFilter(List<String> filters){
        //Log.i("getSelectedSensorTypes", "returns " + type);
        filterSensors(filters);
    }

     void filterSensors(List<String> sensorFilter) {
        for (Map.Entry<Long, SensorMarker> entry : sensorObjectMarkerList.entrySet()) {
            SensorMarker sensorMarker = entry.getValue();

            if(sensorFilter.contains(sensorMarker.getType())) {
                sensorMarker.getMarker().setVisible(true);
            }
            else if(sensorFilter.contains(NONE)) {
                sensorMarker.getMarker().setVisible(true);
            }
            else if(!sensorFilter.contains(sensorMarker.getType())) {
                sensorMarker.getMarker().setVisible(false);
            }

            if(sensorFilter.size() == 0) {
                sensorMarker.getMarker().setVisible(true);
            }
        }
    }

    @Override
    public void getSelectedOtherFilter(List<String> filters) {
        filterOthers(filters);
    }

    private void filterOthers(List<String> otherFilters) {
        for (Map.Entry<Long, SensorMarker> entry : sensorObjectMarkerList.entrySet()) {
            SensorMarker sensorMarker = entry.getValue();

            if(otherFilters.contains(getApplication().getString(R.string.heartbeat_zero)) && sensorMarker.getType().equalsIgnoreCase(getApplication().getString(R.string.heartbeat))) {
                showHeartbeatZero(sensorMarker);
            }

            if(otherFilters.contains(getApplication().getString(R.string.tripped_vibration)) && sensorMarker.getType().equalsIgnoreCase(getApplication().getString(R.string.vibration))) {
                showTrippedVibration(sensorMarker);
            }

            if(otherFilters.contains(getApplication().getString(R.string.dead_battery))) {
                showDeadBattery(sensorMarker);
            }

            if(otherFilters.size() == 0) {
                sensorMarker.getMarker().setVisible(true);
            }
        }

        for (Map.Entry<String, ForceMarker> entry : forceObjectMarkerList.entrySet()) {
            ForceMarker forceMarker = entry.getValue();


            if(otherFilters.size() == 0) {
                forceMarker.getMarker().setVisible(true);
            }
        }
    }

    private void showHeartbeatZero(SensorMarker sensorMarker) {
        Long sensorID = sensorMarkerList.inverse().get(sensorMarker.getMarker());
        SensorData sensorData = sensorDataList.get(sensorID);

        if(sensorData.getSensor_Val() != 0) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    private void showTrippedVibration(SensorMarker sensorMarker) {
        Long sensorID = sensorMarkerList.inverse().get(sensorMarker.getMarker());
        SensorData sensorData = sensorDataList.get(sensorID);

        if(sensorData.getSensor_Val() < 1) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    private void showDeadBattery(SensorMarker sensorMarker) {
        Long sensorID = sensorMarkerList.inverse().get(sensorMarker.getMarker());
        SensorData sensorData = sensorDataList.get(sensorID);

        if(sensorData.getBattery() != 0) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    @Override
    public void getSelectedSensorFilterIndicesBoolean(boolean[] indices){
        sensorFilterIndices = indices;
    }

    @Override
    public void getSelectedForceFilterIndicesBoolean(boolean[] indices){
        forceFilterIndices = indices;
    }

    @Override
    public void getSelectedOtherFilterIndicesBoolean(boolean[] indices) {
        otherFilterIndices = indices;
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMarkerClickListener(this);
        boundsBuilder = new LatLngBounds.Builder();
        getSensorData(null);
        getForceData(null);
        addUserLocation(googleMap);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    bounds = boundsBuilder.build();
                    int padding = 0;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                }
                catch (IllegalStateException e) { //not enough points to build boundsBuilder
                    System.out.println("not enough sensors on map to determine camera boundsBuilder");
                }
            }
        }, 5000);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Long sensorID = sensorMarkerList.inverse().get(marker);
        String forceID = forceMarkerList.inverse().get(marker);

        String message = null;

        if(sensorID != null) {
            message = displaySensorData(sensorID);
        }
        if(forceID != null) {
            message = displayForceData(forceID);
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return true;
    }

    private String displaySensorData(Long sensorID) {
        SensorData sensorData = sensorDataList.get(sensorID);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date((Long) sensorData.getDate_Time()));

        String message =
                "ID: " + sensorData.getSensor_ID() + "\n" +
                        "Type: " + sensorData.getSensor_Type() + "\n" +
                        "Value: " + sensorData.getSensor_Val() + "\n" +
                        "Timestamp: " + timestamp + "\n" +
                        "Health: " + sensorData.getSensorHealth()+ "\n" +
                        "Battery: " + sensorData.getBattery() + "%";

        return message;
    }

    private String displayForceData(String forceID) {
        ForceData forceData = forceDataList.get(forceID);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date((Long) forceData.getDate_Time()));

        String message =
                "ID: " + forceData.getID() + "\n" +
                        "Type: " + forceData.getType() + "\n" +
                        "Name: " + forceData.getName() + "\n" +
                        "Timestamp: " + timestamp + "\n" +
                        "Status: " + forceData.getStatus();

        return message;
    }

    private boolean isTrippedVibrationSensor(SensorData sensorData) {
        if (sensorData.getSensor_Type().equals("Vibration") && sensorData.getSensor_Val() > 0)
            return true;
        return false;
    }

    private boolean isDeadHeartRateSensor(SensorData sensorData) {
        if (sensorData.getSensor_Type().equals("HeartRate") && sensorData.getSensor_Val() == 0)
            return true;
        return false;
    }


    public void addSensorMarker(SensorData sensorData, String sensorFilter) {
        Long sensorID = sensorData.getSensor_ID();
        Double latitude = sensorData.getLat();
        Double longitude = sensorData.getLong();
        LatLng position = new LatLng(latitude, longitude);
        String type = sensorData.getSensor_Type();

        //create the google maps marker with the correct symbol
        Marker marker = createSensorMarker(position, sensorData);
        sensorMarkerList.put(sensorID, marker);

        //if the type of the newly added marker is currently filtered out
        if(sensorFilter != null && !type.equalsIgnoreCase(sensorFilter)) {
            marker.setVisible(false);
        }

        SensorMarker sensorMarker = new SensorMarker(sensorID, type, marker);
        sensorObjectMarkerList.put(sensorID, sensorMarker);


        if (bounds == null)
            boundsBuilder.include(new LatLng(latitude, longitude));
        else
            bounds.including(new LatLng(latitude, longitude));
    }

    public Marker createSensorMarker(LatLng position, SensorData sensorData) {
        String type = sensorData.getSensor_Type();
        Marker marker = null;

        if (type.equals("HeartRate")) {
            if (isDeadHeartRateSensor(sensorData)) {
                marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("dead_heartrate", 128, 128))).anchor(0.5f, 0.5f));
            }
            else {
                marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pointer_heart",128,128))).anchor(0.5f, 0.5f));
            }
        }
        else if (type.equals("Asset"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("diamond",128,128))).anchor(0.5f, 0.5f));
        else if (type.equals("Vibration")) {
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("vibration1",128,128))).anchor(0.5f, 0.5f));
            if (isTrippedVibrationSensor(sensorData))
                setMarkerWobble(marker);
        }
        else if (type.equals("Temp"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("thermometer",128,128))).anchor(0.5f, 0.5f));
        else if (type.equals("Moisture"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("water_drop",128,128))).anchor(0.5f, 0.5f));
        else
            System.out.println(type + "this shouldn't happen");

        return marker;
    }

    public long getMarkerCount() {
        return sensorMarkerList.values().size();
    }

    public void addForceMarker(ForceData forceData, String forceFilter) {
        String forceID = forceData.getID();
        Double latitude = forceData.getLat();
        Double longitude = forceData.getLong();
        LatLng position = new LatLng(latitude, longitude);
        String type = forceData.getType();

        //create the google maps marker with the correct symbol
        Marker marker = createForceMarker(position, type);
        forceMarkerList.put(forceID, marker);

        //if the type of the newly added marker is currently filtered out
        if(forceFilter != null && !type.equalsIgnoreCase(forceFilter)) {
            marker.setVisible(false);
        }

        ForceMarker forceMarker = new ForceMarker(forceID, type, marker);
        forceObjectMarkerList.put(forceID, forceMarker);


        if (bounds == null)
            boundsBuilder.include(new LatLng(latitude, longitude));
        else
            bounds.including(new LatLng(latitude, longitude));
    }

    public Marker createForceMarker(LatLng position, String type) {
        Marker marker = null;

        if (type.equals("Company HQ"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("company_hq",100,150))));
        else if (type.equals("Platoon"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("platoon",150,130))));
        else if (type.equals("Squad"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("squad",150,130))));
        else if (type.equals("Enemy Unit"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("enemy_unit",128,128))));
        else if (type.equals("Preplanned Target"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("target",128,128))));
        else
            System.out.println(type + "this shouldn't happen");

        return marker;
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void setMarkerWobble(final Marker marker) {
        if (marker != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, -0.25f, 0.2f, -0.15f, 0.1f, 0.05f, 0f);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = (float) animation.getAnimatedValue();
                        marker.setAnchor(0.5f + v, 0.5f);
                        //marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });
            animatedSensorList.put(marker, valueAnimator);
            valueAnimator.start();
        }
    }

    private void updateSensorMarker(Marker marker, SensorData sensorData) {
        LatLng newPosition = new LatLng(sensorData.getLat(), sensorData.getLong());
        marker.setPosition(newPosition);

        if (sensorData.getSensor_Type().equals("HeartRate")) {
            if (isDeadHeartRateSensor(sensorData)) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("dead_heartrate",128,128)));
            }
            else {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pointer_heart",128,128)));
            }
        }
        else if (sensorData.getSensor_Type().equals("Vibration")) {
            if (animatedSensorList.containsKey(marker)) {
                ValueAnimator valueAnimator = animatedSensorList.get(marker);
                if (valueAnimator != null) {
                    if (valueAnimator.isRunning() && !isTrippedVibrationSensor(sensorData))
                        valueAnimator.pause();
                    if (valueAnimator.isPaused() && isTrippedVibrationSensor(sensorData))
                        valueAnimator.resume();
                    //paused and not tripped, do nothing
                    //running and tripped, do nothing
                }
            }
            else {
                if(isTrippedVibrationSensor(sensorData)) {
                    setMarkerWobble(marker);
                }
            }
        }
    }


    void getSensorData(final String sensorFilter) {
        db.collection("sensors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        SensorData sensorData = documentSnapshot.toObject(SensorData.class);

                        Long sensorID = sensorData.getSensor_ID();

                        if(sensorDataList.containsKey(sensorID)) {
                            if (sensorData.getDate_Time() > sensorDataList.get(sensorID).getDate_Time()) {
                                sensorDataList.get(sensorID).setAll(sensorData);
                                sensorMarkerList.get(sensorID).setPosition(new LatLng(sensorData.getLat(), sensorData.getLong()));
                                updateSensorMarker(sensorObjectMarkerList.get(sensorID).getMarker(), sensorData);
                            }
                        }
                        else {
                            sensorDataList.put(sensorID, sensorData);

                            addSensorMarker(sensorData, sensorFilter);
                        }
                    }
                }
            }
        });
    }

    void getForceData(final String forceFilter) {
        db.collection("forces").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ForceData forceData = documentSnapshot.toObject(ForceData.class);

                        String forceID = forceData.getID();

                        if(forceDataList.containsKey(forceID)) {
                            if (forceData.getDate_Time() > forceDataList.get(forceID).getDate_Time()) {
                                forceDataList.get(forceID).setAll(forceData);
                                forceMarkerList.get(forceID).setPosition(new LatLng(forceData.getLat(), forceData.getLong()));
                                forceObjectMarkerList.get(forceID).getMarker().setPosition(new LatLng(forceData.getLat(), forceData.getLong()));
                            }
                        }
                        else {
                            forceDataList.put(forceID, forceData);

                            addForceMarker(forceData, forceFilter);
                        }
                    }
                }
            }
        });
    }
    void addUserLocation(GoogleMap map){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }
    }
}

package com.cs495.battleelite.battleelite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.cs495.battleelite.battleelite.fragments.MapFilterFragment;
import com.cs495.battleelite.battleelite.fragments.NotificationFilterFragment;
import com.cs495.battleelite.battleelite.holders.objects.SensorData;
import com.cs495.battleelite.battleelite.holders.objects.SensorMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nullable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, MapFilterFragment.MapFilterFragmentListener {

    private static final String NONE = "none";
    private GoogleMap mMap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BiMap<Long, SensorData> dataList = HashBiMap.create();
    BiMap<Long, Marker> markerList = HashBiMap.create();
    BiMap<Long, SensorMarker> sensorMarkerList = HashBiMap.create();
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
                MapFilterFragment filter = new MapFilterFragment();
                filter.show(getFragmentManager(), "MapFilterFragment");

            }

        });
    }

    @Override
    public void getSelectedSensorTypeFilter(String type){
        Log.i("getSelectedSensorTypes", "returns " + type);
        filterSensors(type);
    }

    private void filterSensors(String sensorFilter) {
        for (Map.Entry<Long, SensorMarker> entry : sensorMarkerList.entrySet()) {
            SensorMarker sensorMarker = entry.getValue();

            if(sensorFilter.equalsIgnoreCase(sensorMarker.getType())) {
                sensorMarker.getMarker().setVisible(true);
            }
            else if(sensorFilter.equalsIgnoreCase(NONE)) {
                sensorMarker.getMarker().setVisible(true);
            }
            else if(!sensorFilter.equalsIgnoreCase(sensorMarker.getType())) {
                sensorMarker.getMarker().setVisible(false);
            }
        }
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
        Long sensorID = markerList.inverse().get(marker);
        SensorData sensorData = dataList.get(sensorID);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date((Long) sensorData.getDate_Time()));

        String message =
                "ID: " + sensorData.getSensor_ID() + "\n" +
                "Type: " + sensorData.getSensor_Type() + "\n" +
                "Value: " + sensorData.getSensor_Val() + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Health: " + sensorData.getSensorHealth()+ "\n" +
                "Battery: " + sensorData.getBattery() + "%";

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

    public void addSensorMarker(SensorData sensorData, String sensorFilter) {
        Long sensorID = sensorData.getSensor_ID();
        Double latitude = sensorData.getLat();
        Double longitude = sensorData.getLong();
        LatLng position = new LatLng(latitude, longitude);
        String type = sensorData.getSensor_Type();

        //create the google maps marker with the correct symbol
        Marker marker = createSensorMarker(position, type);
        markerList.put(sensorID, marker);

        //if the type of the newly added marker is currently filtered out
        if(sensorFilter != null && !type.equalsIgnoreCase(sensorFilter)) {
            marker.setVisible(false);
        }

        SensorMarker sensorMarker = new SensorMarker(sensorID, type, marker);
        sensorMarkerList.put(sensorID, sensorMarker);


        if (bounds == null)
            boundsBuilder.include(new LatLng(latitude, longitude));
        else
            bounds.including(new LatLng(latitude, longitude));
    }

    public Marker createSensorMarker(LatLng position, String type) {
        Marker marker = null;

        if (type.equals("HeartRate"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pointer_heart",128,128))));
        else if (type.equals("Asset"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("diamond",128,128))));
        else if (type.equals("Vibration"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("vibration1",128,128))));
        else if (type.equals("Temp"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("thermometer",128,128))));
        else if (type.equals("Moisture"))
            marker = mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("water_drop",128,128))));
        else
            System.out.println(type + "this shouldn't happen");

        return marker;
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    void getSensorData(final String sensorFilter) {
        db.collection("sensors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        SensorData sensorData = documentSnapshot.toObject(SensorData.class);

                        Long sensorID = sensorData.getSensor_ID();

                        if(dataList.containsKey(sensorID)) {
                            dataList.get(sensorID).setAll(sensorData);
                            markerList.get(sensorID).setPosition(new LatLng(sensorData.getLat(), sensorData.getLong()));
                            sensorMarkerList.get(sensorID).getMarker().setPosition(new LatLng(sensorData.getLat(), sensorData.getLong()));
                        }
                        else {
                            dataList.put(sensorID, sensorData);

                            addSensorMarker(sensorData, sensorFilter);
                        }
                    }
                }
            }
        });
    }
}

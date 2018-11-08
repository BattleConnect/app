package com.cs495.battleelite.battleelite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.cs495.battleelite.battleelite.fragments.MapFilterFragment;
import com.cs495.battleelite.battleelite.fragments.NotificationFilterFragment;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    Map<Long, Map<String, Object>> sensors; //long = Sensor_ID, Object = most recent sensor data entry
    BiMap<Long, Marker> sensorMarkers;
    HashMap<Long, SensorMarker> markerList = new HashMap<>();
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
        for (Map.Entry<Long, SensorMarker> entry : markerList.entrySet()) {
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
        long sensorID = sensorMarkers.inverse().get(marker);
        Map<String, Object> sensorData = sensors.get(sensorID);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date((long) sensorData.get("Date_Time")));

        String message =
                "ID: " + sensorData.get("Sensor_ID") + "\n" +
                "Type: " + sensorData.get("Sensor_Type") + "\n" +
                "Value: " + sensorData.get("Sensor_Val") + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Health: " + sensorData.get("SensorHealth") + "\n" +
                "Battery: " + sensorData.get("Battery") + "%";

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

    public void addSensorMarker(Map<String, Object> sensorData, String sensorFilter) {
        long sensorID = (long) sensorData.get("Sensor_ID");

        double lat = (double) sensorData.get("Lat");
        double lng = (double) sensorData.get("Long");
        LatLng pos = new LatLng(lat, lng);

        Marker marker = null;

        String type = (String) sensorData.get("Sensor_Type");

        if(sensorFilter != null){
            if(sensorFilter.equals(type)){
                if (type.equals("HeartRate"))
                    marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pointer_heart",128,128))));
                else if (type.equals("Asset"))
                    marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("diamond",128,128))));
                else if (type.equals("Vibration"))
                    marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("vibration1",128,128))));
                else if (type.equals("Temp"))
                    marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("thermometer",128,128))));
                else if (type.equals("Moisture"))
                    marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("water_drop",128,128))));
                else
                    System.out.println(type + "this shouldn't happen");

                sensorMarkers.put(sensorID, marker);
                SensorMarker sensorMarker = new SensorMarker(sensorID, type, marker);
                markerList.put(sensorID, sensorMarker);
            }
            else {
                //do nothing we don't wanna see that sensor
            }
        }
        else {
            if (type.equals("HeartRate"))
                marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pointer_heart",128,128))));
            else if (type.equals("Asset"))
                marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("diamond",128,128))));
            else if (type.equals("Vibration"))
                marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("vibration1",128,128))));
            else if (type.equals("Temp"))
                marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("thermometer",128,128))));
            else if (type.equals("Moisture"))
                marker = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("water_drop",128,128))));
            else
                System.out.println(type + "this shouldn't happen");

            sensorMarkers.put(sensorID, marker);
            SensorMarker sensorMarker = new SensorMarker(sensorID, type, marker);
            markerList.put(sensorID, sensorMarker);
        }


        if (bounds == null)
            boundsBuilder.include(new LatLng(lat, lng));
        else
            bounds.including(new LatLng(lat, lng));
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    void getSensorData(final String sensorFilter) {
        sensors = new HashMap<Long, Map<String, Object>>();
        sensorMarkers = HashBiMap.create();
        db.collection("sensors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    long sensorID = (long) dc.getDocument().get("Sensor_ID");
                    switch (dc.getType()) {
                        case ADDED:
                        case MODIFIED:
                            if (sensors.containsKey(sensorID)) {
                                Map<String, Object> oldSensorData = sensors.get(sensorID);
                                Map<String, Object> newSensorData = dc.getDocument().getData();
                                System.out.println(oldSensorData.get("Date_Time"));
                                System.out.println(newSensorData.get("Date_Time"));

                                long oldDateTime = (long) oldSensorData.get("Date_Time");
                                long newDateTime = (long) newSensorData.get("Date_Time");
                                if (newDateTime > oldDateTime) {
                                    sensors.put(sensorID, newSensorData);
                                    double lat = (double) newSensorData.get("Lat");
                                    double lng = (double) newSensorData.get("Long");
                                    LatLng newPos = new LatLng(lat, lng);
                                    if (sensorMarkers.containsKey(sensorID)) {
                                        if (sensorMarkers.get(sensorID).getPosition() != newPos) {
                                            System.out.println("changing marker location");
                                            sensorMarkers.get(sensorID).setPosition(newPos);
                                            markerList.get(sensorID).setMarker(sensorMarkers.get(sensorID));
                                        }
                                    }
                                    else {
                                        addSensorMarker(newSensorData, sensorFilter);
                                    }
                                }
                            }
                            else {
                                sensors.put(sensorID, dc.getDocument().getData());
                                addSensorMarker(dc.getDocument().getData(), sensorFilter);
                            }
                            break;
                        case REMOVED:
                            //TODO
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        System.out.println("Test------: " + markerList.size());
    }
}

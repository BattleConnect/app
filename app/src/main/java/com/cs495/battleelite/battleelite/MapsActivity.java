package com.cs495.battleelite.battleelite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nullable;
import android.os.Handler;
import android.widget.TextView;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mMap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<Long, Map<String, Object>> sensors; //long = Sensor_ID, Object = most recent sensor data entry
    BiMap<Long, Marker> sensorMarkers;
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
        getSensorData();
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

    public void addSensorMarker(Map<String, Object> sensorData) {
        long sensorID = (long) sensorData.get("Sensor_ID");
        String title = (String) sensorData.get("Sensor_Type");
        double lat = (double) sensorData.get("Lat");
        double lng = (double) sensorData.get("Long");
        LatLng pos = new LatLng(lat, lng);
        Marker marker = mMap.addMarker(new MarkerOptions().position(pos).title(title));
        sensorMarkers.put(sensorID, marker);
        if (bounds == null)
            boundsBuilder.include(new LatLng(lat, lng));
        else
            bounds.including(new LatLng(lat, lng));
    }

    void getSensorData() {
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
                                        if (sensorMarkers.get(sensorID).getPosition() != newPos)
                                            sensorMarkers.get(sensorID).setPosition(newPos);
                                    }
                                    else {
                                        addSensorMarker(newSensorData);
                                    }
                                }
                            }
                            else {
                                sensors.put(sensorID, dc.getDocument().getData());
                                addSensorMarker(dc.getDocument().getData());
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
    }
}

package com.cs495.battleelite.battleelite;

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
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nullable;
import android.os.Handler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<Long, Map<String, Object>> sensors; //long = Sensor_ID, Object = most recent sensor data entry
    Map<Long, Marker> sensorMarkers;
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
        }, 2000);
    }


    public void addSensorMarker(Map<String, Object> sensorData) {
        Long sensorID = (Long) sensorData.get("Sensor_ID");
        String title = (String) sensorData.get("Sensor_Type");
        Double lat = (Double) sensorData.get("Lat");
        Double lng = (Double) sensorData.get("Long");
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
        sensorMarkers = new HashMap<Long, Marker>();
        db.collection("sensors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Long sensorID = (Long) dc.getDocument().get("Sensor_ID");
                    switch (dc.getType()) {
                        case ADDED:
                        case MODIFIED:
                            if (sensors.containsKey(sensorID)) {
                                Map<String, Object> oldSensorData = sensors.get(sensorID);
                                Map<String, Object> newSensorData = dc.getDocument().getData();
                                Double oldDateTime = (Double) oldSensorData.get("Date_Time");
                                Double newDateTime = (Double) newSensorData.get("Date_Time");
                                if (newDateTime > oldDateTime) {
                                    sensors.put(sensorID, newSensorData);
                                    Double lat = (Double) newSensorData.get("Lat");
                                    Double lng = (Double) newSensorData.get("Long");
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

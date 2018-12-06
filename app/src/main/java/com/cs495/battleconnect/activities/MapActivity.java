package com.cs495.battleconnect.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cs495.battleconnect.R;
import com.cs495.battleconnect.fragments.MapFilterFragment;
import com.cs495.battleconnect.fragments.MapForceDialogFragment;
import com.cs495.battleconnect.fragments.MapSensorDialogFragment;
import com.cs495.battleconnect.holders.objects.ForceData;
import com.cs495.battleconnect.holders.objects.ForceMarker;
import com.cs495.battleconnect.holders.objects.SensorData;
import com.cs495.battleconnect.holders.objects.SensorMarker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The activity that manages sensors and force icons on a map.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, MapFilterFragment.MapFilterFragmentListener {

    private GoogleMap mMap;
    View mapView;
    LatLngBounds.Builder boundsBuilder;
    LatLngBounds bounds = null;
    private SupportMapFragment mMapFragment;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    BiMap<Long, SensorData> sensorIdToSensorData = HashBiMap.create();
    BiMap<Long, Marker> sensorIdToMarker = HashBiMap.create();
    BiMap<Long, SensorMarker> sensorIdToSensorMarker = HashBiMap.create();

    public SensorData getSensorData(long sensorId) {
        return sensorIdToSensorData.get(sensorId);
    }
    public long getSensorId(SensorData sensorData) { return sensorIdToSensorData.inverse().get(sensorData); }

    public Marker getMarker(long sensorId) {
        return sensorIdToMarker.get(sensorId);
    }
    public long getSensorId(Marker marker) {
        return sensorIdToMarker.inverse().get(marker);
    }
    public Set<Marker> getSensorMarkers() {
        return sensorIdToMarker.values();
    }

    public SensorMarker getSensorMarker(long sensorId) { return sensorIdToSensorMarker.get(sensorId); }
    public long getSensorId(SensorMarker sensorMarker) { return sensorIdToSensorMarker.inverse().get(sensorMarker); }

    BiMap<String, ForceData> forceIdToForceData = HashBiMap.create();
    BiMap<String, Marker> forceIdToMarker = HashBiMap.create();
    BiMap<String, ForceMarker> forceIdToForceMarker = HashBiMap.create();

    public ForceData getForceData(String forceId) {
        return forceIdToForceData.get(forceId);
    }
    public String getForceId(ForceData forceData) { return forceIdToForceData.inverse().get(forceData); }

    public Marker getMarker(String forceId) {
        return forceIdToMarker.get(forceId);
    }
    public String getForceId(Marker marker) {
        return forceIdToMarker.inverse().get(marker);
    }
    public Set<Marker> getForceMarkers() {
        return forceIdToMarker.values();
    }

    public ForceMarker getForceMarker(String forceId) {
        return forceIdToForceMarker.get(forceId);
    }
    public String getForceId(ForceMarker forceMarker) { return forceIdToForceMarker.inverse().get(forceMarker); }

    //Keeps tracks of animators associated with markers, if any. Tripped vibration sensors shake for example.
    Map<Marker, ValueAnimator> markerToAnimator = new HashMap();

    public ValueAnimator getAnimator(Marker marker) {
        return markerToAnimator.get(marker);
    }

    private static final String NONE = "none";
    boolean[] toggleFilterIndices;
    boolean[] sensorTypeFilterIndices;
    boolean[] forceTypeFilterIndices;
    boolean[] otherFilterIndices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeMap();

        configureFilterButton();
    }

    /**
     * Attaches a listener to the filter button, so that when the filter button is clicked it will bring up the filter dialog.
     */
    private void configureFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFilterFragment filter = MapFilterFragment.newInstance(toggleFilterIndices, sensorTypeFilterIndices, forceTypeFilterIndices, otherFilterIndices);
                filter.show(getFragmentManager(), "MapFilterFragment");

            }

        });

    }

    /**
     * Loads the google map fragment.
     */
    private void initializeMap() {
        mMapFragment = SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, mMapFragment);
        transaction.commit();
    }

    /**
     * Gets the selected filters associated with toggling sensors and forces.
     * @param filters
     */
    @Override
    public void getSelectedToggleFilter(List<String> filters) {
        if(filters.size() != 0) toggleData(filters);
    }

    /**
     * Based on the selected filters, will toggle the sensors or forces on the map on/off.
     * @param toggles
     */
    public void toggleData(List<String> toggles) {
        if(toggles.contains(getResources().getString(R.string.forces))) {
            for (Map.Entry<String, ForceMarker> entry : forceIdToForceMarker.entrySet()) {
                ForceMarker forceMarker = entry.getValue();
                forceMarker.getMarker().setVisible(true);
            }
        } else {
            for (Map.Entry<String, ForceMarker> entry : forceIdToForceMarker.entrySet()) {
                ForceMarker forceMarker = entry.getValue();
                forceMarker.getMarker().setVisible(false);
            }
        }

        if(toggles.contains(getResources().getString(R.string.sensors))) {
            for (Map.Entry<Long, SensorMarker> entry : sensorIdToSensorMarker.entrySet()) {
                SensorMarker sensorMarker = entry.getValue();
                sensorMarker.getMarker().setVisible(true);
            }
        } else {
            for (Map.Entry<Long, SensorMarker> entry : sensorIdToSensorMarker.entrySet()) {
                SensorMarker sensorMarker = entry.getValue();
                sensorMarker.getMarker().setVisible(false);
            }
        }
    }

    /**
     * Gets the selected filters associated with filtering forces by their type.
     * @param filters
     */
    @Override
    public void getSelectedForceTypeFilter(List<String> filters) {
        //Log.i("getSelectedSensorTypes", "returns " + type);
        if(filters.size() != 0) filterForces(filters);
    }

    /**
     * Filters the forces shown on the map based on their type.
     * @param forceFilter
     */
    public void filterForces(List<String> forceFilter) {
        for (Map.Entry<String, ForceMarker> entry : forceIdToForceMarker.entrySet()) {
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

//            if(forceFilter.size() == 0) {
//                forceMarker.getMarker().setVisible(true);
//            }
        }
    }

    /**
     * Gets the selected filters associated with filtering sensors by their type.
     * @param filters
     */
    @Override
    public void getSelectedSensorTypeFilter(List<String> filters){
        //Log.i("getSelectedSensorTypes", "returns " + type);
        if(filters.size() != 0) filterSensors(filters);
    }

    /**
     * Filters the sensors shown on the map based on their type.
     * @param sensorFilter
     */
     public void filterSensors(List<String> sensorFilter) {
        for (Map.Entry<Long, SensorMarker> entry : sensorIdToSensorMarker.entrySet()) {
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

    //            if(sensorFilter.size() == 0) {
    //                sensorMarker.getMarker().setVisible(true);
    //            }
        }
    }

    /**
     * Gets the filter options selected that fall under the category of "Other filters".
     * @param filters
     */
    @Override
    public void getSelectedOtherFilter(List<String> filters) {
        if(filters.size() != 0) filterOthers(filters);
    }

    /**
     * Filters the sensors and forces on the map based on the filters selected under the "Other filters" category.
     * @param otherFilters
     */
    private void filterOthers(List<String> otherFilters) {
        for (Map.Entry<Long, SensorMarker> entry : sensorIdToSensorMarker.entrySet()) {
            SensorMarker sensorMarker = entry.getValue();

            if(otherFilters.contains(getApplication().getString(R.string.heartbeat_zero)) && sensorMarker.getType().equals("Heart Rate")) {
                hideIfNotDeadHeartRateSensor(sensorMarker);
            }

            if(otherFilters.contains(getApplication().getString(R.string.tripped_vibration)) && sensorMarker.getType().equals("Vibration")) {
                hideIfNotTrippedVibrationSensor(sensorMarker);
            }

            if(otherFilters.contains(getApplication().getString(R.string.dead_battery))) {
                hideIfNotDeadBattery(sensorMarker);
            }

//            if(otherFilters.size() == 0) {
//                sensorMarker.getMarker().setVisible(true);
//            }
        }

        for (Map.Entry<String, ForceMarker> entry : forceIdToForceMarker.entrySet()) {
            ForceMarker forceMarker = entry.getValue();


//            if(otherFilters.size() == 0) {
//                forceMarker.getMarker().setVisible(true);
//            }
        }
    }

    /**
     * Hides heart rate sensors with nonzero values.
     * @param sensorMarker
     */
    private void hideIfNotDeadHeartRateSensor(SensorMarker sensorMarker) {
        Long sensorID = getSensorId(sensorMarker);
        SensorData sensorData = getSensorData(sensorID);

        if(sensorData.getSensor_Val() != 0) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    /**
     * Hides vibration sensors that have not been tripped.
     * @param sensorMarker
     */
    private void hideIfNotTrippedVibrationSensor(SensorMarker sensorMarker) {
        Long sensorID = sensorIdToMarker.inverse().get(sensorMarker.getMarker());
        SensorData sensorData = sensorIdToSensorData.get(sensorID);

        if(sensorData.getSensor_Val() < 1) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    /**
     * Hides sensors that don't have dead batteries.
     * @param sensorMarker
     */
    private void hideIfNotDeadBattery(SensorMarker sensorMarker) {
        Long sensorID = sensorIdToMarker.inverse().get(sensorMarker.getMarker());
        SensorData sensorData = sensorIdToSensorData.get(sensorID);

        if(sensorData.getBattery() != 0) {
            sensorMarker.getMarker().setVisible(false);
        }
    }

    /**
     * Gets the indices of the filters selected associated with toggling sensors and forces on/off.
     * @param indices
     */
    @Override
    public void getSelectedToggleFilterIndicesBoolean(boolean[] indices) {
        toggleFilterIndices = indices;
    }

    /**
     * Gets the indices of the filters selected associated with sensors.
     * @param indices
     */
    @Override
    public void getSelectedSensorFilterIndicesBoolean(boolean[] indices){
        sensorTypeFilterIndices = indices;
    }

    /**
     * Gets the indices of the filters selected associated with forces.
     * @param indices
     */
    @Override
    public void getSelectedForceFilterIndicesBoolean(boolean[] indices){
        forceTypeFilterIndices = indices;
    }

    /**
     * Gets the indices of the filters selected that fall under the cateogory of "Other filters".
     * @param indices
     */
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

        //get sensor data and force data from firebase
        getSensorDataFromFirebase(null);
        getForceDataFromFirebase(null);

        addUserLocation(googleMap);

        mapView = mMapFragment.getView();

        //place current location button in top left corner
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,30,30);
        }

        //show loading dialog
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Retrieving data. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //every 1 second, check if there is enough points to zoom in on. give up after 5 seconds.
        final Handler handler = new Handler();
        final long startTime = System.currentTimeMillis();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //check if there is enough markers on the map to zoom in on
                if (sensorIdToMarker.size() + forceIdToMarker.size() >= 10) {
                    try {
                        bounds = boundsBuilder.build();
                        int padding = 0;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.moveCamera(cu);
                        dialog.dismiss();
                    }
                    catch (IllegalStateException e) { //not enough points to build boundsBuilder
                        dialog.dismiss();
                    }
                }
                else {
                    if (System.currentTimeMillis() - startTime < 5000) {
                        handler.postDelayed(this, 1000);
                    }
                    else {
                        System.out.println("not enough sensors on map to determine camera boundsBuilder");
                        dialog.dismiss();
                    }
                }
            }
        }, 1000);
    }

    /**
     * Overrides what happens when you click on a marker. When you click on a marker, an alert dialog is shown giving you more information about the specific sensor/force.
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Long sensorId = getSensorId(marker);
        String forceId = getForceId(marker);

        if (sensorId != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SensorData sensorData = sensorIdToSensorData.get(sensorId);
            MapSensorDialogFragment fragment = MapSensorDialogFragment.newInstance(sensorData);
            fragment.show(ft, "dialog");
        }
        else if (forceId != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ForceData forceData = forceIdToForceData.get(forceId);
            MapForceDialogFragment fragment = MapForceDialogFragment.newInstance(forceData);
            fragment.show(ft, "dialog");
        }
        return true;
    }

    /**
     * Determines if a vibration sensor has been tripped or not.
     * @param sensorData
     * @return
     */
    private boolean isTrippedVibrationSensor(SensorData sensorData) {
        if (sensorData.getSensor_Type().equals("Vibration") && sensorData.getSensor_Val() > 0)
            return true;
        return false;
    }

    /**
     * Determines if a heart rate sensor is a dead heart rate sensor or not.
     * @param sensorData
     * @return
     */
    private boolean isDeadHeartRateSensor(SensorData sensorData) {
        if (sensorData.getSensor_Type().equals("HeartRate") && sensorData.getSensor_Val() == 0)
            return true;
        return false;
    }


    /**
     * Adds a marker on the map that is associated with a sensor.
     * @param sensorData
     * @param sensorFilter If there is a filter currently selected, apply that filter to the marker being added.
     */
    public void addSensorMarker(SensorData sensorData, String sensorFilter) {
        Long sensorID = sensorData.getSensor_ID();
        Double latitude = sensorData.getLat();
        Double longitude = sensorData.getLong();
        LatLng position = new LatLng(latitude, longitude);
        String type = sensorData.getSensor_Type();

        //create the google maps marker with the correct symbol
        Marker marker = createSensorMarker(position, sensorData);
        sensorIdToMarker.put(sensorID, marker);

        //if the type of the newly added marker is currently filtered out
        if(sensorFilter != null && type != sensorFilter) {
            marker.setVisible(false);
        }

        SensorMarker sensorMarker = new SensorMarker(sensorID, type, marker);
        sensorIdToSensorMarker.put(sensorID, sensorMarker);

        if (bounds == null)
            boundsBuilder.include(new LatLng(latitude, longitude));
        else
            bounds.including(new LatLng(latitude, longitude));
    }

    /**
     * Creates a marker for a sensor.
     * @param position The marker's location.
     * @param sensorData
     * @return
     */
    public Marker createSensorMarker(LatLng position, SensorData sensorData) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(position).icon(getSensorMapIcon(sensorData, 128, 128)).anchor(0.5f, 0.5f));
        if (sensorData.getSensor_Type().equals("Vibration") && isTrippedVibrationSensor(sensorData))
            setMarkerShake(marker);

        return marker;
    }

    //Stores every icon that is created so that the same icon isn't created over and over again.
    Map<Triplet<String, Integer, Integer>, Bitmap> icons = new HashMap<>();

    /**
     * Resizes an icon.
     * @param iconName The desired icon.
     * @param width
     * @param height
     * @return
     */
    public Bitmap resizeMapIcon(String iconName, int width, int height){
        Triplet<String, Integer, Integer> iconDesc = new Triplet<>(iconName, width, height);
        //If an icon with the same specifications has been created before, obtain it from the icons dictionary.
        if (icons.containsKey(iconDesc)) {
            return icons.get(iconDesc);
        }
        else {
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            icons.put(iconDesc, resizedBitmap);
            return resizedBitmap;
        }
    }

    /**
     * Gets the correct icon for a sensor based on the sensor's type.
     * @param sensorData
     * @param width
     * @param height
     * @return
     */
    public BitmapDescriptor getSensorMapIcon(SensorData sensorData, int width, int height) {
        String sensorType = sensorData.getSensor_Type();

        if (sensorType.equals("HeartRate")) {
            if (isDeadHeartRateSensor(sensorData)) {
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("dead_heartrate", width, height));
            }
            else
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("pointer_heart", width, height));
        }
        else if (sensorType.equals("Moisture"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("water_drop", width, height));
        else if (sensorType.equals("Vibration"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("vibration1", width, height));
        else if (sensorType.equals("Asset"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("diamond", width, height));
        else if (sensorType.equals("Temp"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("thermometer", width, height));

        return null;
    }

    /**
     * Gets the right icon for a force based on the force's type.
     * @param forceType
     * @return
     */
    public BitmapDescriptor getForceIcon(String forceType) {
        if (forceType.equals("Platoon"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("platoon", 150, 130));
        else if (forceType.equals("Squad"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("squad", 150, 130));
        else if (forceType.equals("Enemy Unit"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("enemy_unit", 128, 128));
        else if (forceType.equals("Preplanned Target"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("target", 128, 128));
        else if (forceType.equals("Company HQ"))
            return BitmapDescriptorFactory.fromBitmap(resizeMapIcon("company_hq", 100, 150));

        return null;
    }

    /**
     * Gets the total number of sensor markers on the map.
     * @return
     */
    public long getSensorMarkerCount() {
        return sensorIdToMarker.values().size();
    }

    /**
     * Adds a marker to the map that is associated with a particular force.
     * @param forceData
     * @param forceFilter If there is a filter currently selected, apply that filter to the marker being added.
     */
    public void addForceMarker(ForceData forceData, String forceFilter) {
        String forceID = forceData.getID();
        Double latitude = forceData.getLat();
        Double longitude = forceData.getLong();
        LatLng position = new LatLng(latitude, longitude);
        String type = forceData.getType();

        //create the google maps marker with the correct symbol
        Marker marker = createForceMarker(position, type);
        forceIdToMarker.put(forceID, marker);

        //if the type of the newly added marker is currently filtered out
        if(forceFilter != null && !type.equalsIgnoreCase(forceFilter)) {
            marker.setVisible(false);
        }

        ForceMarker forceMarker = new ForceMarker(forceID, type, marker);
        forceIdToForceMarker.put(forceID, forceMarker);


        if (bounds == null)
            boundsBuilder.include(new LatLng(latitude, longitude));
        else
            bounds.including(new LatLng(latitude, longitude));
    }

    /**
     * Creates a marker that is associated with a force.
     * @param position
     * @param type
     * @return
     */
    public Marker createForceMarker(LatLng position, String type) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(position).icon(getForceIcon(type)));
        return marker;
    }


    /**
     * Adds a shaking animation to a particular map marker.
     * @param marker
     */
    public void setMarkerShake(final Marker marker) {
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
                    } catch (Exception ex) {
                        Log.d("MapActivity", "Marker animation failed to update.");
                    }
                }
            });
            markerToAnimator.put(marker, valueAnimator);
            valueAnimator.start();
        }
    }

    /**
     * Updates a marker on the map associated with a sensor based on new data about that sensor.
     * @param marker The marker's current sensor.
     * @param sensorData The new data about the sensor.
     */
    private void updateSensorMarker(Marker marker, SensorData sensorData) {
        LatLng newPosition = new LatLng(sensorData.getLat(), sensorData.getLong());
        marker.setPosition(newPosition);

        if (sensorData.getSensor_Type().equals("HeartRate")) {
            marker.setIcon(getSensorMapIcon(sensorData,128,128));
        }
        else if (sensorData.getSensor_Type().equals("Vibration")) {
            if (markerToAnimator.containsKey(marker)) {
                ValueAnimator valueAnimator = markerToAnimator.get(marker);
                if (valueAnimator != null) {
                    //animation is running, but the sensor has no longer been tripped: pause the animation
                    if (valueAnimator.isRunning() && !isTrippedVibrationSensor(sensorData))
                        valueAnimator.pause();
                    //animation is not running and the sensor has been tripped: resume the animation
                    if (valueAnimator.isPaused() && isTrippedVibrationSensor(sensorData))
                        valueAnimator.resume();
                    //animation is not running but the sensor has not been tripped: do nothing
                    //sensor has been tripped but the animation is already running: do nothing
                }
            }
            else {
                if(isTrippedVibrationSensor(sensorData)) {
                    setMarkerShake(marker);
                }
            }
        }
    }

    /**
     * Continuously retrieves sensor data from Firebase.
     * @param sensorFilter Want to pass currently selected filters associated with sensors to addSensorMarker() .
     */
    void getSensorDataFromFirebase(final String sensorFilter) {
        db.collection("sensors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        SensorData sensorData = documentSnapshot.toObject(SensorData.class);

                        Long sensorID = sensorData.getSensor_ID();

                        if(sensorIdToSensorData.containsKey(sensorID)) {
                            if (sensorData.getDate_Time() > sensorIdToSensorData.get(sensorID).getDate_Time()) {
                                sensorIdToSensorData.get(sensorID).setAll(sensorData);
                                sensorIdToMarker.get(sensorID).setPosition(new LatLng(sensorData.getLat(), sensorData.getLong()));
                                updateSensorMarker(sensorIdToSensorMarker.get(sensorID).getMarker(), sensorData);
                            }
                        }
                        else {
                            sensorIdToSensorData.put(sensorID, sensorData);
                            addSensorMarker(sensorData, sensorFilter);
                        }
                    }
                }
            }
        });
    }

    /**
     * Continuously retrieves force data from Firebase.
     * @param forceFilter Want to pass currently selected filters associated with sensors to addForceMarker() .
     */
    void getForceDataFromFirebase(final String forceFilter) {
        db.collection("forces").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ForceData forceData = documentSnapshot.toObject(ForceData.class);

                        String forceID = forceData.getID();

                        if(forceIdToForceData.containsKey(forceID)) {
                            if (forceData.getDate_Time() > forceIdToForceData.get(forceID).getDate_Time()) {
                                forceIdToForceData.get(forceID).setAll(forceData);
                                forceIdToMarker.get(forceID).setPosition(new LatLng(forceData.getLat(), forceData.getLong()));
                                forceIdToForceMarker.get(forceID).getMarker().setPosition(new LatLng(forceData.getLat(), forceData.getLong()));
                            }
                        }
                        else {
                            forceIdToForceData.put(forceID, forceData);
                            addForceMarker(forceData, forceFilter);
                        }
                    }
                }
            }
        });
    }

    /**
     * Ensures that the map gives the user the option to see their current location.
     * @param map
     */
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

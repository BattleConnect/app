package com.cs495.battleconnect;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.cs495.battleconnect.activities.MapActivity;
import com.cs495.battleconnect.holders.objects.ForceData;
import com.cs495.battleconnect.holders.objects.SensorData;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static android.support.constraint.Constraints.TAG;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;

/**
 * This class tests filtering on the map, marker animations on the map, and the number of markers displayed on the map.
 */
@RunWith(AndroidJUnit4.class)
public class MapActivityTests {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MapActivity> mActivityRule =
            new ActivityTestRule<>(MapActivity.class);


    /**
     * Tests that the number of sensors displayed on the map matches the number of unique sensors in firebase.
     */
    @Test
    public void testNumberOfSensorsDisplayed() {
        final HashSet<Long> sensorIds = new HashSet<>(); //contains all of the unique sensors Ids
        db.collection("sensors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SensorData sensorData = document.toObject(SensorData.class);
                                if (!sensorIds.contains(sensorData.getSensor_ID())) {
                                    sensorIds.add(sensorData.getSensor_ID());
                                }
                            }
                        } else {
                            Log.d("MapActivityTests", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //give the MapActivity time to add sensors to the map
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        assertEquals(mActivityRule.getActivity().getSensorMarkerCount(), (long) sensorIds.size());
    }

    /**
     * Tests that the number of forces displayed on the map matches the number of unique forces in firebase.
     */
    @Test
    public void testNumberOfForcesDisplayed() {
        final HashSet<String> forceIds = new HashSet<>(); //contains all of the unique force Ids
        db.collection("forces")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ForceData forceData = document.toObject(ForceData.class);
                                if (!forceIds.contains(forceData.getID())) {
                                    forceIds.add(forceData.getID());
                                }
                            }
                        } else {
                            Log.d("MapActivityTests", "Error getting documents: ", task.getException());
                        }
                    }
                });

        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        //give the MapActivity time to add sensors to the map
        assertEquals(mActivityRule.getActivity().getForceMarkerCount(), (long) forceIds.size());
    }

    /**
     * Tests that tripped vibration sensors have animated markers on the map.
     */
    @Test
    public void testTrippedVibrationSensorAnimation() {
        //Give the MapActivity time to initialize.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        //example tripped vibration sensor data
        final SensorData sensorData = new SensorData((long) 200, 66.7715, 33.8163, "Good", (long) 123456, "Vibration", (long) 2, (long) 35);
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //add the tripped vibration sensor to the map
                    mActivityRule.getActivity().addSensorMarker(sensorData, null);
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //give the map time to add the new marker
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        Marker marker = mActivityRule.getActivity().getMarker(sensorData.getSensor_ID());
        ValueAnimator valueAnimator = mActivityRule.getActivity().getAnimator(marker);
        assert(valueAnimator != null);
        assert(valueAnimator.isRunning());
    }

    boolean sensorTypeFilterTestSucceeded = true; //becomes false if the test fails at any point
    boolean completedSensorTypeFilter = false; //keeps track of whether the test has completed or not

    /**
     * Tets the ability to filter sensors by type on the map.
     */
    @Test
    public void testSensorTypeFilter() {
        //Give the MapActivity time to add markers to the map.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep threw exception.", e);
        }

        final List<String> sensorTypeFilter = new ArrayList<>();
        sensorTypeFilter.add("HeartRate");
        sensorTypeFilter.add("Vibration");
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().filterSensors(sensorTypeFilter);
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (marker.isVisible()) {
                            long sensorId = mActivityRule.getActivity().getSensorId(marker);
                            SensorData sensorData = mActivityRule.getActivity().getSensorData(sensorId);
                            if(sensorData.getSensor_Type() == "HeartRate" || sensorData.getSensor_Type() == "Vibration")
                                sensorTypeFilterTestSucceeded = false;
                        }
                        else {
                            long sensorId = mActivityRule.getActivity().getSensorId(marker);
                            SensorData sensorData = mActivityRule.getActivity().getSensorData(sensorId);
                            if (sensorData.getSensor_Type() != "HeartRate" && sensorData.getSensor_Type() != "Vibration")
                                sensorTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //sleep until the test has finished going through all the markers
        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("MapActivityTests","Thread sleep exception.", e);
            }
        }
        assert(sensorTypeFilterTestSucceeded);
    }

    boolean forceTypeFilterTestSucceeded = true; //becomes false if any marker has the wrong visibility
    boolean completedForceTypeFilter = false; //keeps track of whether the test has completed or not

    /**
     * Test the ability to filter by force type on the map.
     */
    @Test
    public void testForceTypeFilter() {
        //Give the MapActivity time to add markers to the map.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        final List<String> forceTypeFilter = new ArrayList<>();
        forceTypeFilter.add("Squad");
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().filterForces(forceTypeFilter);
                    for (Marker marker : mActivityRule.getActivity().getForceMarkers()) {
                        if (marker.isVisible()) {
                            String forceId = mActivityRule.getActivity().getForceId(marker);
                            ForceData forceData = mActivityRule.getActivity().getForceData(forceId);
                            if(forceData.getType() == "Squad")
                                forceTypeFilterTestSucceeded = false;
                        }
                        else {
                            String forceId = mActivityRule.getActivity().getForceId(marker);
                            ForceData forceData = mActivityRule.getActivity().getForceData(forceId);
                            if (forceData.getType() != "Squad")
                                forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedForceTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //sleep until the test is done looping through the markers
        while (!completedForceTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("MapActivityTests","Thread sleep exception.", e);
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    /**
     * Tests the ability to toggle force markers.
     */
    @Test
    public void testForceViewToggle() {
        //Give the MapActivity time to add markers to the map.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        final List<String> toggleFilter = new ArrayList<>();
        toggleFilter.add(String.valueOf(R.string.forces));
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().toggleData(toggleFilter);
                    for (Marker marker : mActivityRule.getActivity().getForceMarkers()) {
                        if (!marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //sleep until the test is done looping through the markers
        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("MapActivityTests","Thread sleep exception.", e);
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    /**
     * Tests the ability to toggle sensor markers on the map.
     */
    @Test
    public void testSensorViewToggle() {
        //Give the MapActivity time to add markers to the map.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        final List<String> toggleFilter = new ArrayList<>();
        toggleFilter.add(String.valueOf(R.string.sensors));
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().toggleData(toggleFilter);
                    for (Marker marker : mActivityRule.getActivity().getForceMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (!marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //sleep until the test has looped through all the markers
        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("MapActivityTests","Thread sleep exception.", e);
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    /**
     * Tests that both sensor and force markers are displayed when sensors and forces are both toggled on.
     */
    @Test
    public void testForceAndSensorViewToggle() {
        //Give the MapActivity time to add markers to the map.
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            Log.d("MapActivityTests","Thread sleep exception.", e);
        }

        final List<String> toggleFilter = new ArrayList<>();
        toggleFilter.add(String.valueOf(R.string.forces));
        toggleFilter.add(String.valueOf(R.string.sensors));
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().toggleData(toggleFilter);
                    for (Marker marker : mActivityRule.getActivity().getForceMarkers()) {
                        if (!marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (!marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {
            Log.d("MapActivityTests","runOnUiThread failed.", t);
        }

        //sleep until the test is done looping through the markers
        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("MapActivityTests","Thread sleep exception.", e);
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }


}

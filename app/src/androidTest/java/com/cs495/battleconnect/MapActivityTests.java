package com.cs495.battleconnect;

import android.animation.ValueAnimator;
import android.os.Handler;
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

@RunWith(AndroidJUnit4.class)
public class MapActivityTests {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MapActivity> mActivityRule =
            new ActivityTestRule<>(MapActivity.class);


    boolean moveOn = false;
    //makes sure that the number of unique sensors in the database matches the number of sensors being displayed on the map
    @Test
    public void testNumberOfSensorsDisplayed() {
        System.out.println("running testNumberOfSensorsDisplayed");
        final HashSet<Long> sensorIds = new HashSet<>();
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    moveOn = true;
                                }
                            }, 1000 * 10);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        while (!moveOn);
            assertEquals(mActivityRule.getActivity().getSensorMarkerCount(), (long) sensorIds.size());
    }

    @Test
    public void testTrippedVibrationSensorAnimation() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
        }

        final SensorData sensorData = new SensorData((long) 200, 66.7715, 33.8163, "Good", (long) 123456, "Vibration", (long) 2, (long) 35);
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().addSensorMarker(sensorData, null);
                }
            });
        }
        catch (Throwable t) {

        }


        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            System.out.println("not good");
        }

        Marker marker = mActivityRule.getActivity().getMarker(sensorData.getSensor_ID());
        ValueAnimator valueAnimator = mActivityRule.getActivity().getAnimator(marker);
        assert(valueAnimator != null);
        assert(valueAnimator.isRunning());
    }

    boolean sensorTypeFilterTestSucceeded = true;
    boolean completedSensorTypeFilter = false;

    @Test
    public void testSensorTypeFilter() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
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

        }

        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("not good");
            }
        }
        assert(sensorTypeFilterTestSucceeded);
    }

    boolean forceTypeFilterTestSucceeded = true;
    boolean completedForceTypeFilter = false;

    @Test
    public void testForceTypeFilter() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
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

        }

        while (!completedForceTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("not good");
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    @Test
    public void testForceViewToggle() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
        }

        final List<String> toggleFilter = new ArrayList<>();
        toggleFilter.add(String.valueOf(R.string.forces));
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivityRule.getActivity().toggleData(toggleFilter);
                    for (Marker marker : mActivityRule.getActivity().getForceMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = true;
                        }
                        else {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = false;
                        }
                        else {
                            forceTypeFilterTestSucceeded = true;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {

        }

        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("not good");
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    @Test
    public void testSensorViewToggle() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
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
                        else {
                            forceTypeFilterTestSucceeded = true;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = true;
                        }
                        else {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {

        }

        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("not good");
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }

    @Test
    public void testForceAndSensorViewToggle() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.out.println("not good");
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
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = true;
                        }
                        else {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    for (Marker marker : mActivityRule.getActivity().getSensorMarkers()) {
                        if (marker.isVisible()) {
                            forceTypeFilterTestSucceeded = true;
                        }
                        else {
                            forceTypeFilterTestSucceeded = false;
                        }
                    }
                    completedSensorTypeFilter = true;
                }
            });
        }
        catch (Throwable t) {

        }

        while (!completedSensorTypeFilter) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("not good");
            }
        }
        assert(forceTypeFilterTestSucceeded);
    }


}

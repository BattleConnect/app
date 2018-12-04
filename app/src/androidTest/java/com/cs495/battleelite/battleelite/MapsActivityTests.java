package com.cs495.battleelite.battleelite;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.cs495.battleelite.battleelite.holders.objects.ForceData;
import com.cs495.battleelite.battleelite.holders.objects.SensorData;
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
public class MapsActivityTests {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class);


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
            assertEquals(mActivityRule.getActivity().getMarkerCount(), (long) sensorIds.size());
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

        Marker marker = mActivityRule.getActivity().sensorMarkerList.get(sensorData.getSensor_ID());
        ValueAnimator valueAnimator = mActivityRule.getActivity().animatedSensorList.get(marker);
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
                    for (Marker marker : mActivityRule.getActivity().sensorMarkerList.values()) {
                        if (marker.isVisible()) {
                            long sensorId = mActivityRule.getActivity().sensorMarkerList.inverse().get(marker);
                            SensorData sensorData = mActivityRule.getActivity().sensorDataList.get(sensorId);
                            if(sensorData.getSensor_Type() == "HeartRate" || sensorData.getSensor_Type() == "Vibration")
                                sensorTypeFilterTestSucceeded = false;
                        }
                        else {
                            long sensorId = mActivityRule.getActivity().sensorMarkerList.inverse().get(marker);
                            SensorData sensorData = mActivityRule.getActivity().sensorDataList.get(sensorId);
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
                    for (Marker marker : mActivityRule.getActivity().forceMarkerList.values()) {
                        if (marker.isVisible()) {
                            String forceId = mActivityRule.getActivity().forceMarkerList.inverse().get(marker);
                            ForceData forceData = mActivityRule.getActivity().forceDataList.get(forceId);
                            if(forceData.getType() == "Squad")
                                forceTypeFilterTestSucceeded = false;
                        }
                        else {
                            String forceId = mActivityRule.getActivity().forceMarkerList.inverse().get(marker);
                            ForceData forceData = mActivityRule.getActivity().forceDataList.get(forceId);
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


}

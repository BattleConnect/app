package com.cs495.battleelite.battleelite;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cs495.battleelite.battleelite.holders.objects.SensorData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTests {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class);


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
                                    System.out.println("don't bullshit me");
                                    assertEquals(mActivityRule.getActivity().getMarkerCount(), (long) sensorIds.size());
                                }
                            }, 1000 * 10);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}

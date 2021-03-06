package com.cs495.battleconnect;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.cs495.battleconnect.R;
import com.cs495.battleconnect.activities.SensorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.constraint.Constraints.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Tests the sensor history fragment.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SensorHistoryTest {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean moveOn = false;
    boolean result = false;

    @Rule
    public ActivityTestRule<SensorActivity> mActivityRule = new ActivityTestRule<SensorActivity>(SensorActivity.class) {};

    /**
     * Tests that the sensor history fragment opens when a sensor item is selected when exploring sensor data.
     */
    @Test
    public void openSensorActivity() {
        db.collection("sensors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onView(ViewMatchers.withId(R.id.sensor_list))
                                            .perform(scrollTo(), RecyclerViewActions.actionOnItemAtPosition(0, click()));

                                    onView(withId(R.id.graph)).check(matches(isDisplayed()));
                                }
                            }, 1000 * 10);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

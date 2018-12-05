package com.cs495.battleconnect;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.cs495.battleconnect.R;
import com.cs495.battleconnect.activities.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * This Espresso test builds on {@link OpenActivity} to use a custom intent when starting the
 * activity. The limitation here is that all {@code &#x40;Test} methods in the class are left using
 * the same intent.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class OpenActivity {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<HomeActivity>(HomeActivity.class) {};

    @Test
    public void openSensorActivity() {
        onView(ViewMatchers.withId(R.id.view_button)).perform(click());
        onView(withId(R.id.sensor_list)).check(matches(isDisplayed()));
    }

    @Test
    public void openAlertsActivity() {
        onView(withId(R.id.alerts_button)).perform(click());
        onView(withId(R.id.notification_list)).check(matches(isDisplayed()));
    }

    @Test
    public void openMakeReport() {
        onView(withId(R.id.input_button)).perform(click());
        onView(withId(R.id.inputComment)).check(matches(isDisplayed()));
    }

//    @Test
//    public void openSoldierData() {
//        onView(withId(R.id.soldier_button)).perform(click());
//        onView(withId(R.id.notification_list)).check(matches(isDisplayed()));
//    }


//    @Test
//    public void openMap() {
//        onView(withId(R.id.map_button)).perform(click());
//        onView(withId(R.id.map)).check(matches(isDisplayed()));
//    }
}
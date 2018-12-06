package com.cs495.battleconnect;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cs495.battleconnect.adapters.SensorAdapter;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;
import com.cs495.battleconnect.responses.SensorResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SensorFilterTest {
    SensorRecyclerViewFragment fragment;
    List<SensorResponse> sensorList = new ArrayList();
    List<String> filterList = new ArrayList();
    SensorAdapter adapter;
    private Context instrumentationCtx;

    @Before
    public void setUp(){
        fragment = new SensorRecyclerViewFragment();

        sensorList.add(new SensorResponse(1541060296, 33.81604, 66.7767, 77, "Good", 378357, "HeartRate", 0));
        sensorList.add(new SensorResponse(341060296, 33.81604, 66.7767, 96, "Poor", 459294, "Vibration", 10));
        sensorList.add(new SensorResponse(154143296, 33.81604, 66.7767, 71, "Fair", 383745, "Asset", 77));
        sensorList.add(new SensorResponse(1569370296, 33.81604, 66.7767, 85, "Good", 191234, "Temp", 34));
        sensorList.add(new SensorResponse(1519365296, 33.81604, 66.7767, 0, "Good", 982371, "Moisture", 63));
        sensorList.add(new SensorResponse(341060296, 33.81604, 66.7767, 96, "Poor", 387531, "Vibration", 0));
        sensorList.add(new SensorResponse(1519365296, 33.81604, 66.7767, 55, "Good", 234958, "Moisture", 63));
        sensorList.add(new SensorResponse(1519365296, 33.81604, 66.7767, 10, "Good", 383741, "Moisture", 63));
        sensorList.add(new SensorResponse(154143296, 33.81604, 66.7767, 71, "Fair", 456742, "Asset", 77));

        adapter = new SensorAdapter(fragment, sensorList);

    }


    //sets up list with one heartrate sensor with a zero value and ensures it is the only thing left in the filtered list
    @Test
    public void testHeartBeatZero(){
        filterList.add("Heartbeat=0");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getSensor_Val(), 0);
    }

    //sets up list with one tripped vibration w/ value 10 and ensures it is the last thing in the filtered list
    @Test
    public void testTrippedVibration(){
        filterList.add("Tripped Vibration Sensor");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getSensor_Val(), 10);
    }

    //tests that the only sensor with a dead battery is left in filtered list
    @Test
    public void testDeadBattery(){
        filterList.add("Dead Battery");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getBattery(), 0);
    }


    //the next 5 tests test that when the list is filtered by sensor type, only the sensor type being filtered on appears in the list
    @Test
    public void testHeartRate(){
        filterList.add("HeartRate");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "HeartRate");
    }

    @Test
    public void testVibration(){
        filterList.add("Vibration");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Vibration");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Vibration");
    }

    @Test
    public void testMoisture(){
        filterList.add("Moisture");
        adapter.filter(filterList);
        assertEquals(3, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(2).getSensor_Type(), "Moisture");
    }

    @Test
    public void testAsset(){
        filterList.add("Asset");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");

    }

    @Test
    public void testTemp(){
        filterList.add("Temp");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Temp");
    }
}

package com.cs495.battleconnect;

import android.content.Context;
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

/**
 * Tests the ability to filter sensor data when exploring sensor data.
 */
@RunWith(AndroidJUnit4.class)
public class SensorFilterTest {
    SensorRecyclerViewFragment fragment;
    List<SensorResponse> sensorList = new ArrayList();
    List<String> filterList = new ArrayList();
    SensorAdapter adapter;
    private Context instrumentationCtx;

    /**
     * Add example sensor data.
     */
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

    /**
     * Tests the ability to filter for dead heart rate sensors.
     */
    @Test
    public void testHeartBeatZero(){
        filterList.add("Heartbeat=0");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getSensor_Val(), 0);
    }

    /**
     * Tests the ability to filter for tripped vibration sensors.
     */
    @Test
    public void testTrippedVibration(){
        filterList.add("Tripped Vibration Sensor");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getSensor_Val(), 10);
    }

    /**
     * Tests the ability to filter for sensors with dead batteries.
     */
    @Test
    public void testDeadBattery(){
        filterList.add("Dead Battery");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals((int)adapter.getFilteredList().get(0).getBattery(), 0);
    }


    /**
     * Tests the ability to filter for heart rate sensors.
     */
    @Test
    public void testHeartRate(){
        filterList.add("HeartRate");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "HeartRate");
    }

    /**
     * Tests the ability to filter for vibration sensors.
     */
    @Test
    public void testVibration(){
        filterList.add("Vibration");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Vibration");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Vibration");
    }

    /**
     * Tests the ability to filter for moisture sensors.
     */
    @Test
    public void testMoisture(){
        filterList.add("Moisture");
        adapter.filter(filterList);
        assertEquals(3, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(2).getSensor_Type(), "Moisture");
    }

    /**
     * Tests the ability to filter for asset sensors.
     */
    @Test
    public void testAsset(){
        filterList.add("Asset");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");

    }

    /**
     * Tests the ability to filter for temperature sensors.
     */
    @Test
    public void testTemp(){
        filterList.add("Temp");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Temp");
    }
}

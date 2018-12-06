package com.cs495.battleconnect;

import android.support.test.runner.AndroidJUnit4;
import com.cs495.battleconnect.adapters.SensorAdapter;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;
import com.cs495.battleconnect.responses.SensorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the ability to search sensor data when exploring sensor data.
 */
@RunWith(AndroidJUnit4.class)
public class SensorSearchTest {
    SensorRecyclerViewFragment fragment;
    List<SensorResponse> sensorList = new ArrayList();
    SensorAdapter adapter;

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
        sensorList.add(new SensorResponse(154143296, 33.81604, 66.7767, 71, "Fair", 456742, "Asset", 77331));

        adapter = new SensorAdapter(fragment, sensorList);

    }

    /**
     * Tests the ability to search for heart rate sensors.
     */
    @Test
    public void testHeartRate(){
        adapter.search("HeartRate");
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "HeartRate");
    }

    /**
     * Tests the ability to search for vibration sensors.
     */
    @Test
    public void testVibration(){
        adapter.search("Vibration");
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Vibration");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Vibration");
    }

    /**
     * Tests the ability to search for moisture sensors.
     */
    @Test
    public void testMoisture(){
        adapter.search("Moisture");
        assertEquals(3, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(1).getSensor_Type(), "Moisture");
        assertEquals(adapter.getFilteredList().get(2).getSensor_Type(), "Moisture");
    }

    /**
     * Tests the ability to search for asset sensors.
     */
    @Test
    public void testAsset(){
        adapter.search("Asset");
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Asset");

    }

    /**
     * Tests the ability to search for temp sensors.
     */
    @Test
    public void testTemp(){
        adapter.search("Temp");
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getSensor_Type(), "Temp");
    }

    /**
     * Tests the ability to search the latitude field.
     */
    @Test
    public void testLat(){
        adapter.search("3.81");
        assertEquals(9, adapter.getFilteredList().size());
    }

    /**
     * Tests the ability to search the longitude field.
     */
    @Test
    public void testLong(){
        adapter.search(".7767");
        assertEquals(9, adapter.getFilteredList().size());
    }

    /**
     * Tests the ability to search the health field.
     */
    @Test
    public void testHealth(){
        adapter.search("Fair");
        assertEquals(2, adapter.getFilteredList().size());
    }

    /**
     * Tests the ability to search the id field..
     */
    @Test
    public void testId(){
        adapter.search("387531");
        assertEquals(1, adapter.getFilteredList().size());
    }

    /**
     * Tests the ability to search the value field.
     */
    @Test
    public void testValue(){
        adapter.search("77331");
        assertEquals(1, adapter.getFilteredList().size());
    }
}

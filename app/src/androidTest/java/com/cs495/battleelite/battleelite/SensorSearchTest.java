package com.cs495.battleelite.battleelite;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cs495.battleelite.battleelite.FilterAdapter;
import com.cs495.battleelite.battleelite.fragments.SensorRecyclerViewFragment;
import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SensorSearchTest {
    SensorRecyclerViewFragment fragment;
    List<SensorResponse> sensorList = new ArrayList();
    List<String> filterList = new ArrayList();


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



    }

    //the 5 tests verify that when a valid sensor type is searched for, only the items with that sensor type occur in the list

    @Test
    public void testHeartRate(){
        setUp();
        FilterAdapter adapter = new FilterAdapter(fragment, sensorList);
        adapter.search("HeartRate");
        assertEquals(1, adapter.filteredList.size());
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "HeartRate");
    }

    @Test
    public void testVibration(){
        setUp();
        FilterAdapter adapter = new FilterAdapter(fragment, sensorList);
        adapter.search("Vibration");
        assertEquals(2, adapter.filteredList.size());
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "Vibration");
        assertEquals(adapter.filteredList.get(1).getSensor_Type(), "Vibration");
    }

    @Test
    public void testMoisture(){
        setUp();
        FilterAdapter adapter = new FilterAdapter(fragment, sensorList);
        adapter.search("Moisture");
        assertEquals(3, adapter.filteredList.size());
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "Moisture");
        assertEquals(adapter.filteredList.get(1).getSensor_Type(), "Moisture");
        assertEquals(adapter.filteredList.get(2).getSensor_Type(), "Moisture");
    }

    @Test
    public void testAsset(){
        setUp();
        FilterAdapter adapter = new FilterAdapter(fragment, sensorList);
        adapter.search("Asset");
        assertEquals(2, adapter.filteredList.size());
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "Asset");
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "Asset");

    }

    @Test
    public void testTemp(){
        setUp();
        FilterAdapter adapter = new FilterAdapter(fragment, sensorList);
        adapter.search("Temp");
        assertEquals(1, adapter.filteredList.size());
        assertEquals(adapter.filteredList.get(0).getSensor_Type(), "Temp");
    }
}

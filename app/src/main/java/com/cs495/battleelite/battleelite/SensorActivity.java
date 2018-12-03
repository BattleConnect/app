package com.cs495.battleelite.battleelite;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cs495.battleelite.battleelite.fragments.FilterDialogFragment;
import com.cs495.battleelite.battleelite.fragments.SensorRecyclerViewFragment;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogFragmentListener {
    SensorRecyclerViewFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.d("START", " HERE");
        fragment = new SensorRecyclerViewFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();

    }

    @Override
    public void getMultipleSelectedSensorFilters(List<String> filters) {
        fragment.updateMultpleSelectedSensorFilters(filters);
    }

    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices) {
        fragment.updateSelectedFilterIndicesBoolean(indices);
    }

    @Override
    public void getOtherSelectedFilterIndicesBoolean(boolean[] indices){
        fragment.updateOtherSelectedFilterIndicesBoolean(indices);
    }
}

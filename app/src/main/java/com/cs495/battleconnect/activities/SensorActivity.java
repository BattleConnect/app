package com.cs495.battleconnect.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cs495.battleconnect.R;
import com.cs495.battleconnect.fragments.SensorFilterDialogFragment;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorFilterDialogFragment.FilterDialogFragmentListener {
    SensorRecyclerViewFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //load the fragment that provides the recycler view for sensors
        fragment = new SensorRecyclerViewFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();

    }


    /**
     * The following three functions are the implementation of the interface declared in the SensorFilterDialogFragment
     * This interface allows the filter values, and indices of those filter values to be passed back and forth from
     * the filter dialog fragment and activity, then allowing the activity to call an update method from withing the
     * recyclerviewfragment which then passes the filter values and indices to the filter adapter which completes
     * the filtering and changes the recyclerview
     */



    /**
     * gets a list of filters from the sensorfilterdialogfragment and calls the recyclerviewfragment update function
     * which then uses the adapter to filter based on those values
     * @param filters
     */
    @Override
    public void getMultipleSelectedSensorFilters(List<String> filters) {
        fragment.updateMultipleSelectedSensorFilters(filters);
    }

    /**
     * gets a list of filter indices from the sensorfilterdialogfragment and passes them along to the recyclerviewfragment
     * @param indices
     */
    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices) {
        fragment.updateSelectedFilterIndicesBoolean(indices);
    }

    /**
     * gets a list of filter indices from the sensorfilterdialogfragment "other" tab and passes them along to the recyclerviewfragment
     * @param indices
     */
    @Override
    public void getOtherSelectedFilterIndicesBoolean(boolean[] indices){
        fragment.updateOtherSelectedFilterIndicesBoolean(indices);
    }
}

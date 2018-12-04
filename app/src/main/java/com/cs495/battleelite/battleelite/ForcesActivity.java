package com.cs495.battleelite.battleelite;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cs495.battleelite.battleelite.fragments.FilterDialogFragment;
import com.cs495.battleelite.battleelite.fragments.ForcesRecyclerViewFragment;

import java.util.List;

public class ForcesActivity extends AppCompatActivity  implements FilterDialogFragment.FilterDialogFragmentListener {
    ForcesRecyclerViewFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forces);

        fragment = new ForcesRecyclerViewFragment();

        FragmentManager manager = getSupportFragmentManager();
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

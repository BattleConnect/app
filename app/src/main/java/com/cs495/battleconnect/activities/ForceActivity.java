package com.cs495.battleconnect.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.fragments.ForceFilterDialogFragment;
import com.cs495.battleconnect.fragments.ForceRecyclerViewFragment;

import java.util.List;

public class ForceActivity extends AppCompatActivity implements ForceFilterDialogFragment.FilterDialogFragmentListener {
    ForceRecyclerViewFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forces);

        fragment = new ForceRecyclerViewFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void getMultipleSelectedSensorFilters(List<String> filters) {
        fragment.updateMultipleSelectedForceFilters(filters);
    }

    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices) {
        fragment.updateSelectedFilterIndicesBoolean(indices);
    }
}

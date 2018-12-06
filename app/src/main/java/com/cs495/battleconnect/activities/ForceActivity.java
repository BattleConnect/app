package com.cs495.battleconnect.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.fragments.ForceFilterDialogFragment;
import com.cs495.battleconnect.fragments.ForceRecyclerViewFragment;

import java.util.List;

/**
 *  This activity lets you explore and filter data about forces. "Explore force data" on the home screen.
 */
public class ForceActivity extends AppCompatActivity implements ForceFilterDialogFragment.FilterDialogFragmentListener {
    ForceRecyclerViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forces);

        //load the fragment that provides the recycler view for forces
        fragment = new ForceRecyclerViewFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
    }

    /**
     * gets a list of filters from the sensorfilterdialogfragment and calls the recyclerviewfragment update function
     * which then uses the adapter to filter based on those values
     * @param filters
     */
    @Override
    public void getMultipleSelectedForceFilters(List<String> filters) {
        fragment.updateMultipleSelectedForceFilters(filters);
    }

    /**
     * gets a list of filter indices from the sensorfilterdialogfragment and passes them along to the recyclerviewfragment
     * @param indices
     */
    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices) {
        fragment.updateSelectedFilterIndicesBoolean(indices);
    }
}

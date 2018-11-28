package com.cs495.battleelite.battleelite;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.cs495.battleelite.battleelite.fragments.SensorRecyclerViewFragment;

public class SensorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        SensorRecyclerViewFragment fragment = new SensorRecyclerViewFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

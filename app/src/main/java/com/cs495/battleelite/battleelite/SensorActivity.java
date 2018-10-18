package com.cs495.battleelite.battleelite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class SensorActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogFragmentListener {
    private static final String TAG = "SensorActivity";
    private static final String SENSORS = "sensors";

    //visual elements
    ProgressBar progressBar;
    RecyclerView sensorList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sensorList = (RecyclerView) findViewById(R.id.sensor_list);

        init();

        //get sensor data
        loadSensorList(null);
        configureFilterButton();
    }

    private void configureFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialogFragment filter = new FilterDialogFragment();
                filter.show(getFragmentManager(), "FilterDialogFragment");

            }

        });
    }

    @Override
    public void getSelectedSensorTypeFilter(String type){
        Log.i("getSelectedSensor", "returns " + type);
        loadSensorList(type);
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sensorList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    private void loadSensorList(String sensorFilter) {
        Query query = db.collection(SENSORS);

        if(sensorFilter != null){
            if(sensorFilter.equals("none")){
                query = db.collection(SENSORS);
            }
            else {
                query = query.whereEqualTo("Sensor_Type", sensorFilter);
            }
        }
        FirestoreRecyclerOptions<SensorResponse> response = new FirestoreRecyclerOptions.Builder<SensorResponse>()
                .setQuery(query, SensorResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SensorResponse, SensorHolder>(response) {
            @Override
            public void onBindViewHolder(SensorHolder holder, int position, SensorResponse model) {
                progressBar.setVisibility(View.GONE);

                //create sensor objects in list
                holder.Date_Time.setText(model.getDate_time().toString());
                holder.Lat.setText(model.getLatitude());
                holder.Long.setText(model.getLongitude());
                holder.Battery.setText(model.getSensor_battery());
                holder.SensorHealth.setText(model.getSensor_health());
                holder.Sensor_ID.setText(model.getSensor_id());
                holder.Sensor_Type.setText(model.getSensor_type());
                holder.Sensor_Val.setText(model.getSensor_value());
            }

            @Override
            public SensorHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.sensor_list_item, group, false);

                return new SensorHolder(view);
            }

            @Override
            public void onDataChanged() {sensorList.getLayoutManager().scrollToPosition(getItemCount() - 1);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        sensorList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}

package com.cs495.battleelite.battleelite;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class SensorActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogFragmentListener {
    private static final String TAG = "SensorActivity";
    private static final String SENSORS = "sensors";

    //visual elements
    ProgressBar progressBar;
    RecyclerView sensorList;

    private FirebaseFirestore db;
    //private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    boolean[] filterIndices;
    List<SensorResponse> sensorData = new ArrayList<>();
    private FilterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sensorList = (RecyclerView) findViewById(R.id.sensor_list);

        init();

        //get sensor data
        loadSensorList();
        configureFilterButton();
    }

    private void configureFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialogFragment filter = FilterDialogFragment.newInstance(filterIndices);
                filter.show(getFragmentManager(), "FilterDialogFragment");

            }

        });
    }

    @Override
    public void getSelectedSensorTypeFilter(String type){
        Log.i("getSelectedSensor", "returns " + type);
        //loadSensorList(type);
    }

    @Override
    public void getMultipleSelectedSensorFilters(List<String> filters){
        Log.i("getMultipleSelectedSens", " returns " + filters);
        adapter.filter(filters);
        //loadSensorList(filters);
    }

    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices){
        filterIndices = indices;
        Log.i("getIndices ", " returns " + indices);
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sensorList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    private void loadSensorList() {
        Log.i(TAG, "START");
        db.collection(SENSORS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<SensorResponse> response = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.i(TAG, " doc date is " + doc.get("Date_Time"));
                    Log.i(TAG, " doc lat is " + doc.get("Lat"));
                    Log.i(TAG, " doc long is " + doc.get("Long"));
                    Log.i(TAG, " doc battery is " + doc.get("Battery"));
                    Log.i(TAG, " doc health is " + doc.get("SensorHealth"));
                    Log.i(TAG, " doc id is " + doc.get("Sensor_ID"));
                    Log.i(TAG, " doc type is " + doc.get("Sensor_Type"));
                    Log.i(TAG, " doc val is " + doc.get("Sensor_Val"));
                    SensorResponse addToList = new SensorResponse(Long.valueOf(doc.get("Date_Time").toString()), Double.valueOf(doc.get("Lat").toString()), Double.valueOf(doc.get("Long").toString()), Long.valueOf(doc.get("Battery").toString()), doc.get("SensorHealth").toString(), Long.valueOf(doc.get("Sensor_ID").toString()), doc.get("Sensor_Type").toString(), Double.valueOf(doc.get("Sensor_Val").toString()));
                   response.add(addToList);
                }
                Log.i(TAG, " responses was: " + response);
                sensorData.addAll(response);
                adapter = new FilterAdapter(SensorActivity.this, sensorData);
                sensorList.setAdapter(adapter);

            }
        });



        Log.i("Sensor Data ", " is: " + sensorData);
        //adapter = new FilterAdapter(this, sensorData);
        //sensorList.setAdapter(adapter);

        Log.i(TAG, "END");

        //Query query = db.collection(SENSORS);
        /*if(filters == null){
            query = db.collection(SENSORS);
        }
        else{
            for(int i=0; i<filters.size(); i++) {
                Log.i("tag ", "filter selected is " + filters.get(i));
                query = query.whereEqualTo("Sensor_Type", filters.get(i));
            }

        }*/

        /*FirestoreRecyclerOptions<SensorResponse> response = new FirestoreRecyclerOptions.Builder<SensorResponse>()
                .setQuery(query, SensorResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SensorResponse, SensorHolder>(response) {

            @Override
            public void onBindViewHolder(SensorHolder holder, int position, SensorResponse model) {
                progressBar.setVisibility(View.GONE);

                //create sensor objects in list

                            holder.Date_Time.setText("Last update: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(model.getDate_Time())));
                            holder.Lat.setText("Latitude: " + String.valueOf(model.getLat()));
                            holder.Long.setText("Longitude: " + String.valueOf(model.getLong()));
                            holder.Battery.setText("Battery: " + String.valueOf(model.getBattery()) + "%");
                            holder.SensorHealth.setText("Health: " + model.getSensorHealth());
                            holder.Sensor_ID.setText("ID: " + String.valueOf(model.getSensor_ID()));
                            holder.Sensor_Type.setText("Type: " + model.getSensor_Type());
                            holder.Sensor_Val.setText("Value: " + String.valueOf(model.getSensor_Val()));

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
        protected void onStart () {
            super.onStart();
            adapter.startListening();
        }

        @Override
        protected void onStop () {
            super.onStop();
            adapter.stopListening();
        }
        */
    }
}

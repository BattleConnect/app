package com.cs495.battleelite.battleelite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.cs495.battleelite.battleelite.adapters.FilterAdapter;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SensorActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogFragmentListener {
    private static final String TAG = "SensorActivity";
    private static final String SENSORS = "sensors";

    //visual elements
    ProgressBar progressBar;
    RecyclerView sensorList;
    SearchView sensorSearch;

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
        sensorSearch = (SearchView) findViewById(R.id.sensor_search);

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

    private void configureSearch(){
        sensorSearch.setIconifiedByDefault(false);
        sensorSearch.setOnQueryTextListener(searchQueryListener);
        sensorSearch.setSubmitButtonEnabled(true);
    }

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            adapter.search(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.search(newText);
            return false;
        }

    };



    @Override
    public void getMultipleSelectedSensorFilters(List<String> filters){
        adapter.filter(filters);
    }

    @Override
    public void getSelectedFilterIndicesBoolean(boolean[] indices){
        filterIndices = indices;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sensorList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    private void loadSensorList() {
        db.collection(SENSORS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<SensorResponse> response = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    SensorResponse addToList = new SensorResponse(Long.valueOf(doc.get("Date_Time").toString()), Double.valueOf(doc.get("Lat").toString()), Double.valueOf(doc.get("Long").toString()), Long.valueOf(doc.get("Battery").toString()), doc.get("SensorHealth").toString(), Long.valueOf(doc.get("Sensor_ID").toString()), doc.get("Sensor_Type").toString(), Double.valueOf(doc.get("Sensor_Val").toString()));
                   response.add(addToList);
                }
                sensorData.addAll(response);
                adapter = new FilterAdapter(SensorActivity.this, sensorData);
                progressBar.setVisibility(View.GONE);
                sensorList.setAdapter(adapter);
                adapter.removeDuplicates();
                configureSearch();
            }
        });


    }
}

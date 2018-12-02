package com.cs495.battleelite.battleelite.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.cs495.battleelite.battleelite.FilterAdapter;
import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class SensorRecyclerViewFragment extends Fragment  {
    private static final String TAG = "SensorActivity";
    private static final String SENSORS = "sensors";

    View view;

    //visual elements
    ProgressBar progressBar;
    RecyclerView sensorList;
    SearchView sensorSearch;

    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    boolean[] filterIndices;
    List<SensorResponse> sensorData = new ArrayList<>();
    private FilterAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Defines the xml file for the fragment
        view =  inflater.inflate(R.layout.fragment_sensor_recycler_view, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        sensorList = (RecyclerView) view.findViewById(R.id.sensor_list);
        sensorSearch = (SearchView) view.findViewById(R.id.sensor_search);

        init();

        //get sensor data
        loadSensorList();
        configureFilterButton();

        return view;
    }

    private void configureFilterButton() {
        final Button filterButton = (Button) view.findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialogFragment filter = FilterDialogFragment.newInstance(filterIndices);
                filter.show(getFragmentManager(), "FilterDialogFragment");
            }
        });
    }

    private void configureSearch() {
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



    public void updateMultpleSelectedSensorFilters(List<String> filters) {
        adapter.filter(filters);
    }


    public void updateSelectedFilterIndicesBoolean(boolean[] indices) {
        filterIndices = indices;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
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

                    SensorResponse addToList = new SensorResponse(Long.valueOf(doc.get("Date_Time").toString()), Double.valueOf(doc.get("Lat").toString()), Double.valueOf(doc.get("Long").toString()), Long.valueOf(doc.get("Battery").toString()), doc.get("SensorHealth").toString(), Long.valueOf(doc.get("Sensor_ID").toString()), doc.get("Sensor_Type").toString(), Double.valueOf(doc.get("Sensor_Val").toString()));
                    response.add(addToList);
                }
                sensorData.addAll(response);
                adapter = new FilterAdapter(SensorRecyclerViewFragment.this, sensorData);
                progressBar.setVisibility(View.GONE);
                sensorList.setAdapter(adapter);
                adapter.removeDuplicates();
                configureSearch();
            }
        });
    }
}

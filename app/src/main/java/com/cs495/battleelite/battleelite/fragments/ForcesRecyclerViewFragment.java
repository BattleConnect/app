package com.cs495.battleelite.battleelite.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.cs495.battleelite.battleelite.adapters.ForcesAdapter;
import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.responses.ForceResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class ForcesRecyclerViewFragment extends Fragment {
    private static final String TAG = "ForcesActivity";
    private static final String FORCES = "forces";

    View view;

    //visual elements
    ProgressBar progressBar;
    RecyclerView forceList;
    SearchView forceSearch;

    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    boolean[] filterIndices;
    boolean[] otherFilterIndices;
    List<ForceResponse> ForceData = new ArrayList<>();
    private ForcesAdapter adapter;

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
        forceList = (RecyclerView) view.findViewById(R.id.sensor_list);
        forceSearch = (SearchView) view.findViewById(R.id.sensor_search);

        init();

        //get force data
        loadForceList();
       // configureFilterButton();

        return view;
    }

    private void configureFilterButton() {
        final Button filterButton = (Button) view.findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialogFragment filter = FilterDialogFragment.newInstance(filterIndices, otherFilterIndices);
                filter.show(getFragmentManager(), "FilterDialogFragment");
            }
        });
    }

    private void configureSearch() {
        forceSearch.setIconifiedByDefault(false);
        forceSearch.setOnQueryTextListener(searchQueryListener);
        forceSearch.setSubmitButtonEnabled(true);
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
        //adapter.filter(filters);
    }


    public void updateSelectedFilterIndicesBoolean(boolean[] indices) {
        filterIndices = indices;
    }

    public void updateOtherSelectedFilterIndicesBoolean(boolean[] indices){
        otherFilterIndices = indices;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        forceList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    private void loadForceList() {
        Log.i(TAG, "START");
        db.collection(FORCES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<ForceResponse> response = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    try {
                        ForceResponse addToList = new ForceResponse(Long.valueOf(doc.get("Date_Time").toString()),
                                Double.valueOf(doc.get("Lat").toString()),
                                Double.valueOf(doc.get("Long").toString()),
                                doc.get("ID").toString(),
                                doc.get("Type").toString(),
                                doc.get("Name").toString(),
                                doc.get("Status").toString());
                        response.add(addToList);
                    }
                    catch (Exception ee){
                        Log.i(TAG,"" + ee);
                    }
                }
                ForceData.addAll(response);
                adapter = new ForcesAdapter( ForcesRecyclerViewFragment.this, ForceData);
                progressBar.setVisibility(View.GONE);
                forceList.setAdapter(adapter);

                configureSearch();
            }
        });
    }
}

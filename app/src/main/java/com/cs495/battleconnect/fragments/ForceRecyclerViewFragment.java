package com.cs495.battleconnect.fragments;

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
import com.cs495.battleconnect.adapters.ForceAdapter;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.responses.ForceResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/**
 * This is the view that shows all the force data in a scrollable list.
 */
public class ForceRecyclerViewFragment extends Fragment {
    private static final String TAG = "ForceActivity";
    private static final String FORCES = "forces";

    View view;

    //visual elements
    ProgressBar progressBar;
    RecyclerView forceList;
    SearchView forceSearch;

    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    boolean[] filterIndices;
    List<ForceResponse> ForceData = new ArrayList<>();
    private ForceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Defines the xml file for the fragment
        view =  inflater.inflate(R.layout.fragment_force_recycler_view, container, false);

        //setting the visual elements
        progressBar = view.findViewById(R.id.progress_bar);
        forceList = view.findViewById(R.id.force_list);
        forceSearch = view.findViewById(R.id.force_search);

        init();

        //get force data
        loadForceList();
        configureFilterButton();

        return view;
    }

    /**
     * Configures the filter button.
     */
    private void configureFilterButton() {
        final Button filterButton = (Button) view.findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForceFilterDialogFragment filter = ForceFilterDialogFragment.newInstance(filterIndices);
                filter.show(getFragmentManager(), "ForceFilterDialogFragment");
            }
        });
    }

    /**
     * Configures the search functionality.
     */
    private void configureSearch() {
        forceSearch.setIconifiedByDefault(false);
        forceSearch.setOnQueryTextListener(searchQueryListener);
        forceSearch.setSubmitButtonEnabled(true);
    }

    /**
     * search query listener that uses the adapter to search when input is detected in the searchview
     */
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

    /**
     * used by the activity to update which filters are selected. the activity uses the interface from the dialog fragment
     * to get the selected values, then calls this update functions to pass the values to the recyclerview adapter
     * @param filters
     */
    public void updateMultipleSelectedForceFilters(List<String> filters) {
        adapter.filter(filters);
    }

    /**
     * used by the activity to set the selected filter indices within the recycler fragment
     * @param indices
     */
    public void updateSelectedFilterIndicesBoolean(boolean[] indices) {
        filterIndices = indices;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        forceList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    /**
     * gets the force data from firestore and adds the data to a list, then sets the adapter
     */
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
                adapter = new ForceAdapter( ForceRecyclerViewFragment.this, ForceData);
                progressBar.setVisibility(View.GONE);
                forceList.setAdapter(adapter);
                adapter.removeDuplicates();
                configureSearch();
            }
        });
    }
}

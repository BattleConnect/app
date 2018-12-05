package com.cs495.battleconnect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.cs495.battleconnect.R;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.cs495.battleconnect.adapters.NotificationAdapter;
import com.cs495.battleconnect.fragments.NotificationFilterFragment;
import com.cs495.battleconnect.responses.NotificationResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class NotificationActivity extends AppCompatActivity implements NotificationFilterFragment.NotificationFilterFragmentListener {
    private static final String TAG = "NotificationActivity";
    private static final String NOTIFICATIONS = "notifications";

    //visual elements
    ProgressBar progressBar;
    RecyclerView notificationList;
    SearchView notificationSearch;
    int selectedSpinnerPosition;

    List<NotificationResponse> notificationData = new ArrayList<>();

    private FirebaseFirestore db;
    private NotificationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.);
        //setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        notificationList = (RecyclerView) findViewById(R.id.notification_list);
        notificationSearch = (SearchView) findViewById(R.id.notification_search);

        init();

        //get notification data
        loadNotificationData();
        configureFilterButton();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        notificationList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        selectedSpinnerPosition = -1;

    }


    private void configureSearch(){
        notificationSearch.setIconifiedByDefault(false);
        notificationSearch.setOnQueryTextListener(searchQueryListener);
        notificationSearch.setSubmitButtonEnabled(true);
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


    private void loadNotificationData() {

        db.collection(NOTIFICATIONS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<NotificationResponse> response = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    NotificationResponse addToList = new NotificationResponse(doc.get("id").toString(), doc.get("message").toString(), doc.get("priority").toString(), doc.get("sender").toString());
                    response.add(addToList);
                }
                notificationData.addAll(response);
                adapter = new NotificationAdapter(NotificationActivity.this, notificationData);
                progressBar.setVisibility(View.GONE);
                notificationList.setAdapter(adapter);
                configureSearch();
            }
        });

    }

    private void configureFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFilterFragment filter = NotificationFilterFragment.newInstance(selectedSpinnerPosition);
                filter.show(getFragmentManager(), "NotificationFilterFragment");

            }

        });
    }

    @Override
    public void getSelectedNotificationPriorityFilter(String type){
        //Log.i("getSelectedNotification", "returns " + type);
        adapter.filter(type);
    }

    @Override
    public void getSelectedState(int position){
        selectedSpinnerPosition = position;
    }


}

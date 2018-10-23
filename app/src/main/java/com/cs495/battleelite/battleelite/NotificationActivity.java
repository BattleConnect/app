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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cs495.battleelite.battleelite.holders.NotificationHolder;
import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.NotificationResponse;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private static final String NOTIFICATIONS = "notifications";

    //visual elements
    ProgressBar progressBar;
    RecyclerView notificationList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.);
        //setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        notificationList = (RecyclerView) findViewById(R.id.notification_list);

        init();

        //get notification data
        loadNotificationData();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        notificationList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

    }

    private void loadNotificationData() {
        Query query = db.collection(NOTIFICATIONS);

        FirestoreRecyclerOptions<NotificationResponse> response = new FirestoreRecyclerOptions.Builder<NotificationResponse>()
                .setQuery(query, NotificationResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<NotificationResponse, NotificationHolder>(response) {
            @Override
            public void onBindViewHolder(NotificationHolder holder, int position, NotificationResponse model) {
                progressBar.setVisibility(View.GONE);

                //create notification objects in list
                holder.id.setText(model.getId());
                holder.sender.setText(model.getSender());
                holder.priority.setText(model.getPriority());
                holder.message.setText(model.getMessage());
            }


            @Override
            public NotificationHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.notification_list_item, group, false);

                return new NotificationHolder(view);
            }

            @Override
            public void onDataChanged() {notificationList.getLayoutManager().scrollToPosition(getItemCount() - 1);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        notificationList.setAdapter(adapter);
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

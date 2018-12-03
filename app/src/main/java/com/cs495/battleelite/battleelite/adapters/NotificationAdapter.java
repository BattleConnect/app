package com.cs495.battleelite.battleelite.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.holders.NotificationHolder;
import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.NotificationResponse;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.opencensus.tags.Tag;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder>  {

    List<NotificationResponse> notificationList, filteredList, filteredListForSearch;
    private Context mContext;

    public NotificationAdapter(Context context, List<NotificationResponse> notificationList){
        this.mContext = context;
        this.notificationList = notificationList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(notificationList);
        this.filteredListForSearch = new ArrayList<>();
        this.filteredListForSearch.addAll(notificationList);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        //create notification objects in list
        NotificationResponse currentItem = filteredList.get(position);

        holder.id.setText(currentItem.getId());
        holder.message.setText(currentItem.getMessage());
        holder.priority.setText(currentItem.getPriority());
        holder.sender.setText(currentItem.getSender());

        switch (currentItem.getPriority()) {
            case "LOW":
                holder.parentLayout.setBackgroundColor(Color.GREEN);
                break;
            case "MEDIUM": holder.parentLayout.setBackgroundColor(Color.YELLOW);
                break;
            case "HIGH": holder.parentLayout.setBackgroundColor(Color.BLUE);
                break;
            case "CRITICAL": holder.parentLayout.setBackgroundColor(Color.RED);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.notification_list_item, group, false);

        return new NotificationHolder(view);
    }


    public void filter(String filter){
        filteredList.clear();
        if(filter == null || filter.equals("none")){
            filteredList = new ArrayList<NotificationResponse>(notificationList);
        }
        for(int i=0; i<notificationList.size(); i++) {
                if(notificationList.get(i).getPriority().toLowerCase().equals(filter.toLowerCase())){
                    filteredList.add(notificationList.get(i));
                }

        }
        filteredListForSearch.clear();
        filteredListForSearch.addAll(filteredList);
        notifyDataSetChanged();
    }


    public void search(String text){
        filteredList.clear();

        if(TextUtils.isEmpty(text)){
            filteredList.addAll(filteredListForSearch);
        }

        else{
            String query = text.toLowerCase();
            for(NotificationResponse item : filteredListForSearch){
                if(item.getPriority().toLowerCase().contains(query) || item.getId().toLowerCase().contains(query) || item.getMessage().toLowerCase().contains(query) || item.getSender().toLowerCase().contains(query)){
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }



}
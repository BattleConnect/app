package com.cs495.battleelite.battleelite.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.holders.NotificationHolder;
import com.cs495.battleelite.battleelite.responses.NotificationResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * The adapter for setting all the values for the notification activity recycler view
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder>  {
    private final String NONE = "none";
    private final String LOW = "LOW";
    private final String MEDIUM = "MEDIUM";
    private final String HIGH = "HIGH";
    private final String CRITICAL = "CRITICAL";
    List<NotificationResponse> notificationList, filteredList, filteredListForSearch;
    private Context mContext;

    /**
     * The constructor for the adapter for setting all the values for the notification activity recycler view
     * @param context
     * @param notificationList
     */
    public NotificationAdapter(Context context, List<NotificationResponse> notificationList){
        this.mContext = context;
        this.notificationList = notificationList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(notificationList);
        this.filteredListForSearch = new ArrayList<>();
        this.filteredListForSearch.addAll(notificationList);
    }

    /**
     * Sets the visual elements values of each item in the notification recycler view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        //create notification objects in list
        NotificationResponse currentItem = filteredList.get(position);

        holder.id.setText("ID: " + currentItem.getId());
        holder.priority.setText("Priority: " + currentItem.getPriority());
        holder.sender.setText("Sender: " + currentItem.getSender());
        holder.message.setText("Message: " + currentItem.getMessage());

        //Change the background color of each item depending on its priority
        switch (currentItem.getPriority()) {
            case LOW:
                holder.parentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.alert_low));
                break;
            case MEDIUM: holder.parentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.alert_medium));
                break;
            case HIGH: holder.parentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.alert_high));
                break;
            case CRITICAL: holder.parentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.alert_critical));
                break;
        }


    }

    /**
     * Returns the number of items in the recycler view
     * @return
     */
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Creates the recycler view and displays it on screen
     * @param group
     * @param i
     * @return
     */
    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.notification_list_item, group, false);

        return new NotificationHolder(view);
    }


    /**
     * Filters the recyclerview content based on the filters selected
     * @param filter
     */
    public void filter(String filter){
        filteredList.clear();
        if(filter == null || filter.equals(NONE)){
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


    /**
     * Searches the items in the recycler view to find the corresponding item
     * @param text
     */
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

    /**
     * Returns the list of items in the recyclerview that match the filter selected
     * @return
     */
    public List<NotificationResponse> getFilteredList(){
        return filteredList;
    }

}
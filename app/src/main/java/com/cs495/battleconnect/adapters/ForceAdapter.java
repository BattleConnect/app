package com.cs495.battleconnect.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs495.battleconnect.R;

import com.cs495.battleconnect.fragments.ForceRecyclerViewFragment;
import com.cs495.battleconnect.holders.ForceHolder;

import com.cs495.battleconnect.responses.ForceResponse;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The adapter for setting all the values for the force activity recycler view
 */

public class ForceAdapter extends RecyclerView.Adapter<ForceHolder> {
    private static final String TAG = "ForceAdapter";
    private final String GOOD = "good";
    private final String Force_ID = "Force_ID";
    private final String NONE = "none";
    private List<ForceResponse> forceList, filteredList, filteredListForSearch;
    private ForceRecyclerViewFragment mFragment;

    public ForceAdapter(ForceRecyclerViewFragment fragment, List<ForceResponse> forceList){
        this.mFragment = fragment;
        this.forceList = forceList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(forceList);
        this.filteredListForSearch = new ArrayList<>();
        this.filteredListForSearch.addAll(forceList);
    }

    @Override
    public void onBindViewHolder(ForceHolder holder, int position) {
        //create sensor objects in list
        final ForceResponse currentItem = filteredList.get(position);

        holder.Date_Time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(currentItem.getDate_Time())));
        holder.Lat.setText(String.valueOf(currentItem.getLat()));
        holder.Long.setText(String.valueOf(currentItem.getLong()));

        holder.name.setText(currentItem.getForce_Name());

        holder.Force_ID.setText(String.valueOf(currentItem.getForce_ID()));
        //holder.Force_ID.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.id));
        holder.Force_Status.setText( String.valueOf(currentItem.getForce_Status()));
        //holder.Force_Type.setImageDrawable();

        //display the sensor type's corresponding icon
        if(currentItem.getForce_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.company))) {
            holder.Force_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.company_hq));
        }
        else if(currentItem.getForce_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.platoon))) {
                holder.Force_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(), R.drawable.platoon));
        } else if(currentItem.getForce_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.squad))) {
            holder.Force_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.squad));
        } else if(currentItem.getForce_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.target))) {
            holder.Force_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.target));
        } else if(currentItem.getForce_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.enemy))) {
            holder.Force_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.enemy_unit));
        }

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
            for(ForceResponse item : filteredListForSearch){
                if(item.getForce_Type().toLowerCase().contains(query)
                        || Long.toString(item.getDate_Time()).toLowerCase().contains(query) || Double.toString(item.getLat()).toLowerCase().contains(query)
                        || Double.toString(item.getLong()).toLowerCase().contains(query) || item.getForce_Name().toLowerCase().contains(query)
                        || item.getForce_ID().toLowerCase().contains(query) ||item.getForce_Status().toLowerCase().contains(query)){
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
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
    public ForceHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.force_list_item, group, false);

        return new ForceHolder(view);
    }

    /**
     * Removes duplicate items from the list with the same ID. Keeps the item with the more recent timestamp.
     */
    public void removeDuplicates(){//takes 1st thing, compares to rest, if id match, check date, swap if needed then delete second item
        Map<String, Long> forceIdToDateTime = new HashMap<>();
        for (int i = 0; i < forceList.size(); i++) {
            String forceId = forceList.get(i).getForce_ID();
            long dateTime = forceList.get(i).getDate_Time();
            if (forceIdToDateTime.containsKey(forceId)) {
                if (dateTime > forceIdToDateTime.get(forceId)) {
                    forceIdToDateTime.put(forceId, dateTime);
                }
            }
            else {
                forceIdToDateTime.put(forceId, dateTime);
            }
        }
        for (Iterator<ForceResponse> iterator = filteredList.iterator(); iterator.hasNext();) {
            ForceResponse x = iterator.next();
            String forceId = x.getForce_ID();
            long dateTime = x.getDate_Time();
            if (forceIdToDateTime.containsKey(forceId) && dateTime != forceIdToDateTime.get(forceId)) {
                iterator.remove();
            }
        }

        notifyDataSetChanged();
    }

    private void swap( int i, int j) {
        ForceResponse temp = filteredList.get(i);
        filteredList.set(i,filteredList.get(j));
        filteredList.set(j, temp);
    }

    /**
     * Filters the recyclerview content based on the filters selected
     * @param filters
     */
    public void filter(List<String> filters){
        filteredList.clear();

        if(filters == null || filters.size() == 0 || filters.get(0).toLowerCase().equals(NONE)){
            filteredList = new ArrayList<>(forceList);
        }

        for(int i=0; i< forceList.size(); i++) {
            for (int j = 0; j < filters.size(); j++) {
                if(forceList.get(i).getForce_Type().equals(filters.get(j))){
                    filteredList.add(forceList.get(i));
                }
            }
        }
        filteredListForSearch.clear();
        filteredListForSearch.addAll(filteredList);
        removeDuplicates();
        notifyDataSetChanged();
    }

    /**
     * Returns the list of items in the recyclerview that match the filter selected
     * @return
     */
    public List<ForceResponse> getFilteredList(){
        return filteredList;
    }
}

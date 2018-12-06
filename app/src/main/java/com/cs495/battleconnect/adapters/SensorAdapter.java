package com.cs495.battleconnect.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs495.battleconnect.R;
import com.cs495.battleconnect.fragments.SensorHistoryFragment;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;
import com.cs495.battleconnect.holders.SensorHolder;
import com.cs495.battleconnect.responses.SensorResponse;
import com.cs495.battleconnect.utility.StringToolkit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The adapter for setting all the values for the sensor activity recycler view
 */
public class SensorAdapter extends RecyclerView.Adapter<SensorHolder>  {
    private static final String TAG = "SensorAdapter";
    private final String GOOD = "good";
    private final String SENSOR_ID = "SENSOR_ID";
    private final String NONE = "none";
    private List<SensorResponse> sensorList, filteredList, filteredListForSearch;
    private SensorRecyclerViewFragment mFragment;

    /**
     * The constructor for the adapter for setting all the values for the sensor activity recycler view
     * @param fragment
     * @param sensorList
     */
    public SensorAdapter(SensorRecyclerViewFragment fragment, List<SensorResponse> sensorList){
        this.mFragment = fragment;
        this.sensorList = sensorList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(sensorList);
        this.filteredListForSearch = new ArrayList<>();
        this.filteredListForSearch.addAll(sensorList);
    }


    /**
     * Sets the visual elements values of each item in the sensor recycler view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SensorHolder holder, int position) {
        //create sensor objects in list
        final SensorResponse currentItem = filteredList.get(position);

        String dateTimeAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(currentItem.getDate_Time()));
        SpannableStringBuilder dateTimeStr = StringToolkit.makeSectionOfTextBold("Last update: " + dateTimeAsString, "Last update:");
        SpannableStringBuilder latStr = StringToolkit.makeSectionOfTextBold("Latitude: " + String.valueOf(currentItem.getLat()), "Latitude:");
        SpannableStringBuilder longStr = StringToolkit.makeSectionOfTextBold("Longitude: " + String.valueOf(currentItem.getLong()), "Longitude:");

        holder.Date_Time.setText(dateTimeStr);
        holder.Lat.setText(latStr);
        holder.Long.setText(longStr);

        holder.Battery.setText(String.valueOf(currentItem.getBattery()) + "%");

        //if the battery is below 20% show the user a red icon instead of a green one
        if(currentItem.getBattery() <= 5) {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.low_battery));

            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.low_battery));
        }
        else if (currentItem.getBattery() <= 20) {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.med_battery));

            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.med_battery));
        }
        else {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.high_battery));

            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.high_battery));
        }



        holder.SensorHealth.setText("Health: " + currentItem.getSensorHealth());
        //if the health is good show the user a green background if the health is bad show a red background
        if(currentItem.getSensorHealth().equalsIgnoreCase(GOOD)) {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.good));
        }
        else {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.bad));
        }

        holder.Sensor_ID.setText("ID: " + String.valueOf(currentItem.getSensor_ID()));
        holder.Sensor_ID.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.id));
        holder.Sensor_Val.setText("Value: " + String.valueOf(currentItem.getSensor_Val()));

        //display the sensor type's corresponding icon
        if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.asset))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.diamond));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.heartbeat))) {
            //if heartrate sensor is 0 then show the dead heartrate icon instead of the regular heartrate icon.
            if(currentItem.getSensor_Val() == 0) {
                holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(), R.drawable.dead_heartrate));
            }
            else {
                holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(), R.drawable.pointer_heart));
            }
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.vibration))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.vibration1));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.moisture))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.water_drop));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.temperature))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.thermometer));
        }

        //setup on click for each item to launch it's sensor history
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong(SENSOR_ID, currentItem.getSensor_ID());
                SensorHistoryFragment fragment = new SensorHistoryFragment();
                fragment.setArguments(bundle);

                FragmentManager manager = mFragment.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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
    public SensorHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.sensor_list_item, group, false);

        return new SensorHolder(view);
    }

    /**
     * Filters the recyclerview content based on the filters selected
     * @param filters
     */
    public void filter(List<String> filters){
        filteredList.clear();

        if(filters == null || filters.size() == 0 || filters.get(0).toLowerCase().equals("NONE")){
            filteredList = new ArrayList<>(sensorList);
        }

        for(int i=0; i<sensorList.size(); i++) {
            for (int j = 0; j < filters.size(); j++) {
                if(sensorList.get(i).getSensor_Type().equals(filters.get(j))){
                    filteredList.add(sensorList.get(i));
                }
                if(filters.get(j).equals("Heartbeat=0")){
                    if(sensorList.get(i).getSensor_Type().equals("HeartRate") && sensorList.get(i).getSensor_Val() == 0){
                        filteredList.add(sensorList.get(i));
                    }
                }
                if(filters.get(j).equals("Tripped Vibration Sensor")){
                    if(sensorList.get(i).getSensor_Type().equals("Vibration") && sensorList.get(i).getSensor_Val() > 0){
                        filteredList.add(sensorList.get(i));
                    }
                }
                if(filters.get(j).equals("Dead Battery")){
                    if(sensorList.get(i).getBattery() == 0){
                        filteredList.add(sensorList.get(i));
                    }
                }
            }
        }
        filteredListForSearch.clear();
        filteredListForSearch.addAll(filteredList);
        removeDuplicates();
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
            for(SensorResponse item : filteredListForSearch){
                if(item.getSensor_Type().toLowerCase().contains(query) || Long.toString(item.getBattery()).toLowerCase().contains(query)
                        || Long.toString(item.getDate_Time()).toLowerCase().contains(query) || Double.toString(item.getLat()).toLowerCase().contains(query)
                        || Double.toString(item.getLong()).toLowerCase().contains(query) || item.getSensorHealth().toLowerCase().contains(query)
                        || Long.toString(item.getSensor_ID()).toLowerCase().contains(query) || Double.toString(item.getSensor_Val()).toLowerCase().contains(query)){
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void swap( int i, int j) {
        SensorResponse temp = filteredList.get(i);
        filteredList.set(i,filteredList.get(j));
        filteredList.set(j, temp);
    }

    /**
     * Removes duplicate items from the list with the same ID
     */
    public void removeDuplicates(){//takes 1st thing, compares to rest, if id match, check date, swap if needed then delete second item
        Map<Long, Long> sensorIdToDateTime = new HashMap<>();
        for (int i = 0; i < sensorList.size(); i++) {
            long sensorId = sensorList.get(i).getSensor_ID();
            long dateTime = sensorList.get(i).getDate_Time();
            if (sensorIdToDateTime.containsKey(sensorId)) {
                if (dateTime > sensorIdToDateTime.get(sensorId)) {
                    sensorIdToDateTime.put(sensorId, dateTime);
                }
            }
            else {
                sensorIdToDateTime.put(sensorId, dateTime);
            }
        }
        for (Iterator<SensorResponse> iterator = filteredList.iterator(); iterator.hasNext();) {
            SensorResponse x = iterator.next();
            long sensorId = x.getSensor_ID();
            long dateTime = x.getDate_Time();
            if (sensorIdToDateTime.containsKey(sensorId) && dateTime != sensorIdToDateTime.get(sensorId)) {
                iterator.remove();
            }
        }

//        for(int i=0; i<filteredList.size(); i++) {
//            SensorResponse x = filteredList.get(i);
//            for(int j = i + 1; j < filteredList.size(); j++) {
//                long xID = x.getSensor_ID(); long tempID = filteredList.get(j).getSensor_ID();
//                if(xID == tempID) {
//                    long xDT = x.getDate_Time(); long tempDT = filteredList.get(j).getDate_Time();
//
//                    try {
//                        if (xDT > tempDT) {
//                            filteredList.remove(j);
//                        } else {
//                            swap(i, j);
//                            filteredList.remove(j);
//                        }
//                    } catch (Exception e) {
//                        Log.d(TAG, e.getMessage());
//                    }
//                    j--;//if match, it deletes the element and put another in its place, so need to check that one too dy decrementing, which the loop will increment, re evaluating the same index
//                }
//            }
//        }

        notifyDataSetChanged();
    }

    /**
     * Returns the list of items in the recyclerview that match the filter selected
     * @return
     */
    public List<SensorResponse> getFilteredList(){
        return filteredList;
    }

}
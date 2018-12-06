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

        //set the background color for the battery info based on the battery %
        if(currentItem.getBattery() <= 5) {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_low));
            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_low));
        }
        else if (currentItem.getBattery() <= 20) {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_med));
            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_med));
        }
        else {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_high));
            holder.bat_icon.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.battery_high));
        }

        holder.SensorHealth.setText("Health: " + currentItem.getSensorHealth());
        //if the health is good show the user a green background if the health is bad show a red background
        if(currentItem.getSensorHealth().equalsIgnoreCase(mFragment.getActivity().getApplication().getString(R.string.health_good))) {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.health_good));
        }
        else if (currentItem.getSensorHealth().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.health_service))) {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.health_service));
        }
        else if (currentItem.getSensorHealth().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.health_EOL))){
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.health_EOL));
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
                if(!filteredList.contains(sensorList.get(i))) {
                    if (sensorList.get(i).getSensor_Type().equals(filters.get(j))) {
                        filteredList.add(sensorList.get(i));
                    }
                    if (filters.get(j).equals("Heartbeat=0")) {
                        if (sensorList.get(i).getSensor_Type().equals("HeartRate") && sensorList.get(i).getSensor_Val() == 0) {
                            filteredList.add(sensorList.get(i));
                        }
                    }
                    if (filters.get(j).equals("Tripped Vibration Sensor")) {
                        if (sensorList.get(i).getSensor_Type().equals("Vibration") && sensorList.get(i).getSensor_Val() > 0) {
                            filteredList.add(sensorList.get(i));
                        }
                    }
                    if (filters.get(j).equals("Health=Service")) {
                        if (sensorList.get(i).getSensorHealth().equals("Service")) {
                            filteredList.add(sensorList.get(i));
                        }
                    }
                    if (filters.get(j).equals("Health=EOL")) {
                        if (sensorList.get(i).getSensorHealth().equals("EOL")) {
                            filteredList.add(sensorList.get(i));
                        }
                    }
                    if (filters.get(j).equals("Dead Battery")) {
                        if (sensorList.get(i).getBattery() == 0) {
                            filteredList.add(sensorList.get(i));
                        }
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
     * FilteredListForSearch is used to hold a previously filtered list that can then be searched.  If the search text is then
     * erased, the recycler can then revert back to this FilteredListForSearch to display the items pertaining to the original
     * filtering, so that simultaneous searching/filtering is enabled
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
        removeDuplicates();
        notifyDataSetChanged();
    }

    /**
     * Removes duplicate items from the list with the same ID. Keeps the item with the more recent timestamp.
     */
    public void removeDuplicates(){ //takes 1st thing, compares to rest, if id match, check date, swap if needed then delete second item
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
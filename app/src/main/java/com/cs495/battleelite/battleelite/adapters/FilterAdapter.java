package com.cs495.battleelite.battleelite;

import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.FragmentManager;
import android.content.Context;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.fragments.SensorHistoryFragment;
import com.cs495.battleelite.battleelite.fragments.SensorRecyclerViewFragment;
import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class FilterAdapter extends RecyclerView.Adapter<SensorHolder>  {

    private List<SensorResponse> sensorList, filteredList, filteredListForSearch;
    private SensorRecyclerViewFragment mFragment;

    public FilterAdapter(SensorRecyclerViewFragment fragment, List<SensorResponse> sensorList){
        this.mFragment = fragment;
        this.sensorList = sensorList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(sensorList);
        this.filteredListForSearch = new ArrayList<>();
        this.filteredListForSearch.addAll(sensorList);
    }

    @Override
    public void onBindViewHolder(SensorHolder holder, int position) {
        //progressBar.setVisibility(View.GONE);

        //create sensor objects in list
        final SensorResponse currentItem = filteredList.get(position);


        holder.Date_Time.setText("Last update: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(currentItem.getDate_Time())));
        holder.Lat.setText("Latitude: " + String.valueOf(currentItem.getLat()));
        holder.Long.setText("Longitude: " + String.valueOf(currentItem.getLong()));



        holder.Battery.setText(String.valueOf(currentItem.getBattery()) + "%");
        if(currentItem.getBattery() > 20) {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.good));
        }
        else {
            holder.Battery.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.bad));
        }



        holder.SensorHealth.setText(currentItem.getSensorHealth());
        if(currentItem.getSensorHealth().equalsIgnoreCase("GOOD")) {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.good));
        }
        else {
            holder.SensorHealth.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.bad));
        }

        holder.Sensor_ID.setText(String.valueOf(currentItem.getSensor_ID()));
        holder.Sensor_ID.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.id));
        holder.Sensor_Val.setText("Value: " + String.valueOf(currentItem.getSensor_Val()));

        if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.asset))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.diamond));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.heartbeat))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.pointer_heart));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.vibration))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.vibration1));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.moisture))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.water_drop));
        } else if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.temperature))) {
            holder.Sensor_Type.setImageDrawable(ContextCompat.getDrawable(mFragment.getActivity().getApplication(),R.drawable.thermometer));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("SENSOR_ID", currentItem.getSensor_ID());
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

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public SensorHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.sensor_list_item, group, false);

        return new SensorHolder(view);
    }

    public void filter(List<String> filters){
        filteredList.clear();
        if(filters == null || filters.size() == 0 || filters.get(0).toLowerCase().equals("none")){
            filteredList = sensorList;
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

    public void removeDuplicates(){//takes 1st thing, compares to rest, if id match, check date, swap if needed then delete second item

        for(int i=0; i<filteredList.size(); i++) {
            SensorResponse x = filteredList.get(i);
            for(int j = i + 1; j < filteredList.size(); j++) {
                long xID = x.getSensor_ID(); long tempID = filteredList.get(j).getSensor_ID();
                if(xID == tempID) {
                   long xDT = x.getDate_Time(); long tempDT = filteredList.get(j).getDate_Time();

                   try {
                       if (xDT > tempDT) {
                           filteredList.remove(j);
                       } else {
                           swap(i, j);
                           filteredList.remove(j);
                       }
                   } catch (Exception e) {
                       //Toast.makeText(null,e.getMessage(),Toast.LENGTH_LONG).show();
                   }
                   j--;//if match, it deletes the element and put another in its place, so need to check that one too dy decrementing, which the loop will increment, re evaluating the same index
                }
            }
        }

        notifyDataSetChanged();
    }

    public List<SensorResponse> getFilteredList(){
        return filteredList;
    }

}
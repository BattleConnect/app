package com.cs495.battleelite.battleelite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.opencensus.tags.Tag;


public class FilterAdapter extends RecyclerView.Adapter<SensorHolder>  {

    List<SensorResponse> sensorList, filteredList, filteredListForSearch;
    private Context mContext;

    public FilterAdapter(Context context, List<SensorResponse> sensorList){
        this.mContext = context;
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
        SensorResponse currentItem = filteredList.get(position);


        holder.Date_Time.setText("Last update: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(currentItem.getDate_Time())));
        holder.Lat.setText("Latitude: " + String.valueOf(currentItem.getLat()));
        holder.Long.setText("Longitude: " + String.valueOf(currentItem.getLong()));
        holder.Battery.setText("Battery: " + String.valueOf(currentItem.getBattery()) + "%");
        holder.SensorHealth.setText("Health: " + currentItem.getSensorHealth());
        holder.Sensor_ID.setText("ID: " + String.valueOf(currentItem.getSensor_ID()));
        holder.Sensor_Type.setText("Type: " + currentItem.getSensor_Type());
        holder.Sensor_Val.setText("Value: " + String.valueOf(currentItem.getSensor_Val()));

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
        if(filters == null || filters.size() == 0){
            filteredList = sensorList;
        }
        for(int i=0; i<sensorList.size(); i++) {
            for (int j = 0; j < filters.size(); j++) {
                if(sensorList.get(i).getSensor_Type().equals(filters.get(j))){
                    filteredList.add(sensorList.get(i));
                }
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
                       //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                   }
                   j--;//if match, it deletes the element and put another in its place, so need to check that one too dy decrementing, which the loop will increment, re evaluating the same index
                }
            }
        }

        notifyDataSetChanged();
    }


}
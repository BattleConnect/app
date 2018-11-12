package com.cs495.battleelite.battleelite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.SensorResponse;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FilterAdapter extends RecyclerView.Adapter<SensorHolder>  {

    List<SensorResponse> sensorList, filteredList;
    private Context mContext;

    public FilterAdapter(Context context, List<SensorResponse> sensorList){
        this.mContext = context;
        this.sensorList = sensorList;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(sensorList);
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
        if(filters == null || filters.size() == 0){


        }
        else{
            removeDuplicates();
        }

        notifyDataSetChanged();
    }

    private void swap( int i, int j) {
        SensorResponse temp = filteredList.get(i);
        filteredList.set(i,filteredList.get(j));
        filteredList.set(j, temp);
    }

    public void removeDuplicates(){

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


                   }
                }
            }
        }
        int g = filteredList.size();
        g++;
        notifyDataSetChanged();
    }



    /*@Override
    public void onDataChanged() {sensorList.getLayoutManager().scrollToPosition(getItemCount() - 1);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }*/

}
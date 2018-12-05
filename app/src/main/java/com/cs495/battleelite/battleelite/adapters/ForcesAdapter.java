package com.cs495.battleelite.battleelite.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.fragments.SensorHistoryFragment;
import com.cs495.battleelite.battleelite.fragments.ForcesRecyclerViewFragment;
import com.cs495.battleelite.battleelite.holders.ForceHolder;
import com.cs495.battleelite.battleelite.holders.ForceHolder;
import com.cs495.battleelite.battleelite.holders.SensorHolder;
import com.cs495.battleelite.battleelite.responses.ForceResponse;
import com.cs495.battleelite.battleelite.responses.SensorResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * The adapter for setting all the values for the force activity recycler view
 */

public class ForcesAdapter extends RecyclerView.Adapter<ForceHolder> {
    private static final String TAG = "ForceAdapter";
    private final String GOOD = "good";
    private final String Force_ID = "Force_ID";
    private final String NONE = "none";
    private List<ForceResponse> forceList, filteredList, filteredListForSearch;
    private ForcesRecyclerViewFragment mFragment;

    public ForcesAdapter(ForcesRecyclerViewFragment fragment, List<ForceResponse> forceList){
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


        holder.Force_ID.setText("ID: " + String.valueOf(currentItem.getForce_ID()));
        //holder.Force_ID.setBackgroundColor(mFragment.getActivity().getApplication().getResources().getColor(R.color.id));
        holder.Force_Status.setText("Value: " + String.valueOf(currentItem.getForce_Status()));
        //holder.Force_Type.setImageDrawable();

        //display the sensor type's corresponding icon
       /* if(currentItem.getSensor_Type().equalsIgnoreCase(mFragment.getActivity().getApplication().getResources().getString(R.string.asset))) {
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
        }*/

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

}

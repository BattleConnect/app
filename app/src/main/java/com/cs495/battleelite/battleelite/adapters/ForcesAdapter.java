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

import com.cs495.battleelite.battleelite.fragments.ForcesRecyclerViewFragment;
import com.cs495.battleelite.battleelite.holders.ForceHolder;
import com.cs495.battleelite.battleelite.holders.ForceHolder;

import com.cs495.battleelite.battleelite.responses.ForceResponse;


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

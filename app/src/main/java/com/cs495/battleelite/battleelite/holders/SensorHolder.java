package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;

public class SensorHolder extends RecyclerView.ViewHolder {
    public TextView date_time;
    public TextView latitude;
    public TextView longitude;
    public TextView sensor_battery;
    public TextView sensor_health;
    public TextView sensor_id;
    public TextView sensor_type;
    public TextView sensor_value;
    public LinearLayout parentLayout;

    public SensorHolder(View itemView) {
        super(itemView);

        date_time = itemView.findViewById(R.id.date_time);
        latitude = itemView.findViewById(R.id.latitude);
        longitude = itemView.findViewById(R.id.longitude);
        sensor_battery = itemView.findViewById(R.id.sensor_battery);
        sensor_health = itemView.findViewById(R.id.sensor_health);
        sensor_id = itemView.findViewById(R.id.sensor_id);
        sensor_type = itemView.findViewById(R.id.sensor_type);
        sensor_value = itemView.findViewById(R.id.sensor_value);

        parentLayout = itemView.findViewById(R.id.parentLayout);
    }

    public void setDate_time(TextView date_time) {
        this.date_time = date_time;
    }

    public void setLatitude(TextView latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(TextView longitude) {
        this.longitude = longitude;
    }

    public void setSensor_battery(TextView sensor_battery) {
        this.sensor_battery = sensor_battery;
    }

    public void setSensor_health(TextView sensor_health) {
        this.sensor_health = sensor_health;
    }

    public void setSensor_id(TextView sensor_id) {
        this.sensor_id = sensor_id;
    }

    public void setSensor_type(TextView sensor_type) {
        this.sensor_type = sensor_type;
    }

    public void setSensor_value(TextView sensor_value) {
        this.sensor_value = sensor_value;
    }
}

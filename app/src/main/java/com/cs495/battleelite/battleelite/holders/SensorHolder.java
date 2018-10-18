package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;

public class SensorHolder extends RecyclerView.ViewHolder {
    public TextView Date_Time;
    public TextView Lat;
    public TextView Long;
    public TextView Battery;
    public TextView SensorHealth;
    public TextView Sensor_ID;
    public TextView Sensor_Type;
    public TextView Sensor_Val;
    public LinearLayout parentLayout;

    public SensorHolder(View itemView) {
        super(itemView);

        Date_Time = itemView.findViewById(R.id.date_time);
        Lat = itemView.findViewById(R.id.latitude);
        Long = itemView.findViewById(R.id.longitude);
        Battery = itemView.findViewById(R.id.sensor_battery);
        SensorHealth = itemView.findViewById(R.id.sensor_health);
        Sensor_ID = itemView.findViewById(R.id.sensor_id);
        Sensor_Type = itemView.findViewById(R.id.sensor_type);
        Sensor_Val = itemView.findViewById(R.id.sensor_value);

        parentLayout = itemView.findViewById(R.id.parentLayout);
    }

    public void setDate_time(TextView date_time) {
        this.Date_Time = date_time;
    }

    public void setLatitude(TextView latitude) {
        this.Lat = latitude;
    }

    public void setLongitude(TextView longitude) {
        this.Long = longitude;
    }

    public void setSensor_battery(TextView sensor_battery) {
        this.Battery = sensor_battery;
    }

    public void setSensor_health(TextView sensor_health) {
        this.SensorHealth = sensor_health;
    }

    public void setSensor_id(TextView sensor_id) {
        this.Sensor_ID = sensor_id;
    }

    public void setSensor_type(TextView sensor_type) {
        this.Sensor_Type = sensor_type;
    }

    public void setSensor_value(TextView sensor_value) {
        this.Sensor_Val = sensor_value;
    }
}

package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;

/**
 * Holds all the visual elements of a sensor item in the sensor recycler view
 */
public class SensorHolder extends RecyclerView.ViewHolder {
    public TextView Date_Time;
    public TextView Lat;
    public TextView Long;
    public TextView Battery;
    public TextView SensorHealth;
    public TextView Sensor_ID;
    public ImageView Sensor_Type;
    public ImageView bat_icon;
    public TextView Sensor_Val;
    public LinearLayout parentLayout;

    /**
     * Constructor for a sensor item
     * @param itemView
     */
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
        bat_icon = itemView.findViewById(R.id.bat_icon);

        parentLayout = itemView.findViewById(R.id.parentLayout);
    }

    /**
     * Set the date time textview
     * @param date_time
     */
    public void setDate_time(TextView date_time) {
        this.Date_Time = date_time;
    }

    /**
     * Set the latitude textview
     * @param latitude
     */
    public void setLatitude(TextView latitude) {
        this.Lat = latitude;
    }

    /**
     * Set the longitude textview
     * @param longitude
     */
    public void setLongitude(TextView longitude) {
        this.Long = longitude;
    }

    /**
     * Set the battery textview
     * @param sensor_battery
     */
    public void setSensor_battery(TextView sensor_battery) {
        this.Battery = sensor_battery;
    }

    /**
     * Set the health textview
     * @param sensor_health
     */
    public void setSensor_health(TextView sensor_health) {
        this.SensorHealth = sensor_health;
    }

    /**
     * Set the sensor id textview
     * @param sensor_id
     */
    public void setSensor_id(TextView sensor_id) {
        this.Sensor_ID = sensor_id;
    }

    /**
     * Set the sensor type imageview
     * @param sensor_type
     */
    public void setSensor_type(ImageView sensor_type) {
        this.Sensor_Type = sensor_type;
    }

    /**
     * Set the sensor value textview
     * @param sensor_value
     */
    public void setSensor_value(TextView sensor_value) {
        this.Sensor_Val = sensor_value;
    }

    /**
     * Set the battery icon imageview
     * @param bat_icon
     */
    public void setBat_icon(ImageView bat_icon) {
        this.bat_icon = bat_icon;
    }
}

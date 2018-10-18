package com.cs495.battleelite.battleelite.responses;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class SensorResponse {
    private Timestamp Date_Time;
    private String Lat;
    private String Long;
    private String Battery;
    private String SensorHealth;
    private String Sensor_ID;
    private String Sensor_Type;
    private String Sensor_Val;

    public SensorResponse() {
    }

    public SensorResponse(Timestamp date_time, String latitude, String longitude, String sensor_battery, String sensor_health, String sensor_id, String sensor_type, String sensor_value) {
        this.Date_Time = date_time;
        this.Lat = latitude;
        this.Long = longitude;
        this.Battery = sensor_battery;
        this.SensorHealth = sensor_health;
        this.Sensor_ID = sensor_id;
        this.Sensor_Type = sensor_type;
        this.Sensor_Val = sensor_value;
    }

    public Timestamp getDate_time() {
        return Date_Time;
    }

    public void setDate_time(Timestamp date_time) {
        this.Date_Time = date_time;
    }

    public String getLatitude() {
        return Lat;
    }

    public void setLatitude(String latitude) {
        this.Lat = latitude;
    }

    public String getLongitude() {
        return Long;
    }

    public void setLongitude(String longitude) {
        this.Long = longitude;
    }

    public String getSensor_battery() {
        return Battery;
    }

    public void setSensor_battery(String sensor_battery) {
        this.Battery = sensor_battery;
    }

    public String getSensor_health() {
        return SensorHealth;
    }

    public void setSensor_health(String sensor_health) {
        this.SensorHealth = sensor_health;
    }

    public String getSensor_id() {
        return Sensor_ID;
    }

    public void setSensor_id(String sensor_id) {
        this.Sensor_ID = sensor_id;
    }

    public String getSensor_type() {
        return Sensor_Type;
    }

    public void setSensor_type(String sensor_type) {
        this.Sensor_Type = sensor_type;
    }

    public String getSensor_value() {
        return Sensor_Val;
    }

    public void setSensor_value(String sensor_value) {
        this.Sensor_Val = sensor_value;
    }
}

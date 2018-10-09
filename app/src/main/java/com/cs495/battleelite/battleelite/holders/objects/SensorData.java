package com.cs495.battleelite.battleelite.holders.objects;

import com.google.firebase.Timestamp;

public class SensorData {
    private Timestamp date_time;
    private String latitude;
    private String longitude;
    private String sensor_battery;
    private String sensor_health;
    private String sensor_id;
    private String sensor_type;
    private String sensor_value;

    public SensorData() {
    }

    public SensorData(Timestamp date_time, String latitude, String longitude, String sensor_battery, String sensor_health, String sensor_id, String sensor_type, String sensor_value) {
        this.date_time = date_time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensor_battery = sensor_battery;
        this.sensor_health = sensor_health;
        this.sensor_id = sensor_id;
        this.sensor_type = sensor_type;
        this.sensor_value = sensor_value;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSensor_battery() {
        return sensor_battery;
    }

    public void setSensor_battery(String sensor_battery) {
        this.sensor_battery = sensor_battery;
    }

    public String getSensor_health() {
        return sensor_health;
    }

    public void setSensor_health(String sensor_health) {
        this.sensor_health = sensor_health;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public String getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(String sensor_type) {
        this.sensor_type = sensor_type;
    }

    public String getSensor_value() {
        return sensor_value;
    }

    public void setSensor_value(String sensor_value) {
        this.sensor_value = sensor_value;
    }
}

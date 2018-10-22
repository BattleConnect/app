package com.cs495.battleelite.battleelite.responses;

public class SensorResponse {
    private long Date_Time;
    private double Lat;
    private double Long;
    private long Battery;
    private String SensorHealth;
    private long Sensor_ID;
    private String Sensor_Type;
    private long Sensor_Val;

    public SensorResponse() {
    }

    public SensorResponse(long date_time, double latitude, double longitude, long sensor_battery, String sensor_health, long sensor_id, String sensor_type, long sensor_val) {
        this.Date_Time = date_time;
        this.Lat = latitude;
        this.Long = longitude;
        this.Battery = sensor_battery;
        this.SensorHealth = sensor_health;
        this.Sensor_ID = sensor_id;
        this.Sensor_Type = sensor_type;
        this.Sensor_Val = sensor_val;
    }

    public long getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(long date_time) { this.Date_Time = date_time; }

    public double getLat() {
        return Lat;
    }

    public void setLat(double latitude) {
        this.Lat = latitude;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double longitude) {
        this.Long = longitude;
    }

    public long getBattery() {
        return Battery;
    }

    public void setBattery(long sensor_battery) {
        this.Battery = sensor_battery;
    }

    public String getSensorHealth() {
        return SensorHealth;
    }

    public void setSensorHealth(String sensor_health) {
        this.SensorHealth = sensor_health;
    }

    public long getSensor_ID() {
        return Sensor_ID;
    }

    public void setSensor_ID(long sensor_id) {
        this.Sensor_ID = sensor_id;
    }

    public String getSensor_Type() { return Sensor_Type; }

    public void setSensor_Type(String sensor_type) {
        this.Sensor_Type = sensor_type;
    }

    public long getSensor_Val() {
        return Sensor_Val;
    }

    public void setSensor_Val(long sensor_val) { this.Sensor_Val = sensor_val; }
}

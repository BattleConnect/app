package com.cs495.battleconnect.responses;

/**
 * Object that holds the sensor object returned from the firestore database
 */
public class SensorResponse {
    private long Date_Time;
    private double Lat;
    private double Long;
    private long Battery;
    private String SensorHealth;
    private long Sensor_ID;
    private String Sensor_Type;
    private double Sensor_Val;

    /**
     * Default constructor
     */
    public SensorResponse() {
    }

    /**
     * Constructor with all fields
     * @param date_time
     * @param latitude
     * @param longitude
     * @param sensor_battery
     * @param sensor_health
     * @param sensor_id
     * @param sensor_type
     * @param sensor_val
     */
    public SensorResponse(long date_time, double latitude, double longitude, long sensor_battery, String sensor_health, long sensor_id, String sensor_type, double sensor_val) {
        this.Date_Time = date_time;
        this.Lat = latitude;
        this.Long = longitude;
        this.Battery = sensor_battery;
        this.SensorHealth = sensor_health;
        this.Sensor_ID = sensor_id;
        this.Sensor_Type = sensor_type;
        this.Sensor_Val = sensor_val;
    }

    /**
     * Grabs the date time
     * @return
     */
    public long getDate_Time() {
        return Date_Time;
    }

    /**
     * Sets the date time
     * @param date_time
     */
    public void setDate_Time(long date_time) { this.Date_Time = date_time; }

    /**
     * Grabs the latitude
     * @return
     */
    public double getLat() {
        return Lat;
    }

    /**
     * Sets the latitude
     * @param latitude
     */
    public void setLat(double latitude) {
        this.Lat = latitude;
    }

    /**
     * Grabs the longitude
     * @return
     */
    public double getLong() {
        return Long;
    }

    /**
     * Sets the longitude
     * @param longitude
     */
    public void setLong(double longitude) {
        this.Long = longitude;
    }

    /**
     * Grabs the battery level
     * @return
     */
    public long getBattery() {
        return Battery;
    }

    /**
     * Sets the battery level
     * @param sensor_battery
     */
    public void setBattery(long sensor_battery) {
        this.Battery = sensor_battery;
    }

    /**
     * Grabs the sensor health
     * @return
     */
    public String getSensorHealth() {
        return SensorHealth;
    }

    /**
     * Sets the sensor health
     * @param sensor_health
     */
    public void setSensorHealth(String sensor_health) {
        this.SensorHealth = sensor_health;
    }

    /**
     * Grabs the sensor id
     * @return
     */
    public long getSensor_ID() {
        return Sensor_ID;
    }

    /**
     * Sets the sensor id
     * @param sensor_id
     */
    public void setSensor_ID(long sensor_id) {
        this.Sensor_ID = sensor_id;
    }

    /**
     * Grabs the sensor type
     * @return
     */
    public String getSensor_Type() { return Sensor_Type; }

    /**
     * Sets the sensor type
     * @param sensor_type
     */
    public void setSensor_Type(String sensor_type) {
        this.Sensor_Type = sensor_type;
    }

    /**
     * Grabs the sensor value
     * @return
     */
    public double getSensor_Val() {
        return Sensor_Val;
    }

    /**
     * Sets the sensor value
     * @param sensor_val
     */
    public void setSensor_Val(long sensor_val) { this.Sensor_Val = sensor_val; }
}

package com.cs495.battleconnect.holders.objects;

/**
 * Object used to hold sensor data from the firestore database
 */
public class SensorData implements java.io.Serializable {
    private Long Date_Time;
    private Double Lat;
    private Double Long;
    private String SensorHealth;
    private Long Sensor_ID;
    private String Sensor_Type;
    private Long Sensor_Val;
    private Long Battery;

    /**
     * Default empty constructor
     */
    public SensorData() {
    }

    /**
     * Constructor with all fields
     * @param date_Time
     * @param lat
     * @param aLong
     * @param sensorHealth
     * @param sensor_ID
     * @param sensor_Type
     * @param sensor_Val
     * @param battery
     */
    public SensorData(Long date_Time, Double lat, Double aLong, String sensorHealth, Long sensor_ID, String sensor_Type, Long sensor_Val, Long battery) {
        Date_Time = date_Time;
        Lat = lat;
        Long = aLong;
        SensorHealth = sensorHealth;
        Sensor_ID = sensor_ID;
        Sensor_Type = sensor_Type;
        Sensor_Val = sensor_Val;
        Battery = battery;
    }

    /**
     * Grabs the date time
     * @return
     */
    public Long getDate_Time() {
        return Date_Time;
    }

    /**
     * Sets the date time
     * @param date_Time
     */
    public void setDate_Time(Long date_Time) {
        Date_Time = date_Time;
    }

    /**
     * Grab the latitude
     * @return
     */
    public Double getLat() {
        return Lat;
    }

    /**
     * Sets the latitude
     * @param lat
     */
    public void setLat(Double lat) {
        Lat = lat;
    }

    /**
     * Grab the longitude
     * @return
     */
    public Double getLong() {
        return Long;
    }

    /**
     * Set the longitude
     * @param aLong
     */
    public void setLong(Double aLong) {
        Long = aLong;
    }

    /**
     * Grab the sensor health
     * @return
     */
    public String getSensorHealth() {
        return SensorHealth;
    }

    /**
     * Set the sensor health
     * @param sensorHealth
     */
    public void setSensorHealth(String sensorHealth) {
        SensorHealth = sensorHealth;
    }

    /**
     * Grab the sensor id
     * @return
     */
    public Long getSensor_ID() {
        return Sensor_ID;
    }

    /**
     * Set the sensor id
     * @param sensor_ID
     */
    public void setSensor_ID(Long sensor_ID) {
        Sensor_ID = sensor_ID;
    }

    /**
     * Grab the sensor type
     * @return
     */
    public String getSensor_Type() {
        return Sensor_Type;
    }

    /**
     * Set the sensor type
     * @param sensor_Type
     */
    public void setSensor_Type(String sensor_Type) {
        Sensor_Type = sensor_Type;
    }

    /**
     * Grab the sensor value
     * @return
     */
    public Long getSensor_Val() {
        return Sensor_Val;
    }

    /**
     * Set the sensor value
     * @param sensor_Val
     */
    public void setSensor_Val(Long sensor_Val) {
        Sensor_Val = sensor_Val;
    }

    /**
     * Grab the sensor battery percentage
     * @return
     */
    public Long getBattery() {
        return Battery;
    }

    /**
     * Set the battery percentage
     * @param battery
     */
    public void setBattery(Long battery) {
        Battery = battery;
    }

    /**
     * Sets all values of sensor data
     * @param data
     */
    public void setAll(SensorData data) {
        setBattery(data.getBattery());
        setLat(data.getLat());
        setLong(data.getLong());
        setDate_Time(data.getDate_Time());
        setSensor_Type(data.getSensor_Type());
        setSensor_Val(data.getSensor_Val());
        setSensorHealth(data.getSensorHealth());
    }
}

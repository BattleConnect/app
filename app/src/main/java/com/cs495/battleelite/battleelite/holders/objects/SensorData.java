package com.cs495.battleelite.battleelite.holders.objects;

public class SensorData implements java.io.Serializable {
    private Long Date_Time;
    private Double Lat;
    private Double Long;
    private String SensorHealth;
    private Long Sensor_ID;
    private String Sensor_Type;
    private Long Sensor_Val;
    private Long Battery;

    public SensorData() {
    }

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

    public Long getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(Long date_Time) {
        Date_Time = date_Time;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    public String getSensorHealth() {
        return SensorHealth;
    }

    public void setSensorHealth(String sensorHealth) {
        SensorHealth = sensorHealth;
    }

    public Long getSensor_ID() {
        return Sensor_ID;
    }

    public void setSensor_ID(Long sensor_ID) {
        Sensor_ID = sensor_ID;
    }

    public String getSensor_Type() {
        return Sensor_Type;
    }

    public void setSensor_Type(String sensor_Type) {
        Sensor_Type = sensor_Type;
    }

    public Long getSensor_Val() {
        return Sensor_Val;
    }

    public void setSensor_Val(Long sensor_Val) {
        Sensor_Val = sensor_Val;
    }

    public Long getBattery() {
        return Battery;
    }

    public void setBattery(Long battery) {
        Battery = battery;
    }

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

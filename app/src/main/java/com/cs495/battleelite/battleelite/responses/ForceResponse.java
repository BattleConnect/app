package com.cs495.battleelite.battleelite.responses;

public class ForceResponse {
    private long Date_Time;
    private double Lat;
    private double Long;
    private long Force_ID;
    private String  Force_Name;
    private String  status;
    private String  Force_Type;


    public ForceResponse() {
    }

    public ForceResponse(long date_time, double latitude, double longitude, long id, String type, String name, String status) {
        this.Date_Time = date_time;
        this.Lat = latitude;
        this.Long = longitude;
        this.Force_Name = name;
        this.status = status;
        this.Force_ID = id;
        this.Force_Type = type;

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

    public long getForce_ID() {
        return Force_ID;
    }

    public void setForce_ID(long force_id) {
        this.Force_ID = force_id;
    }

    public String getForce_Name() { return Force_Name; }

    public void setForce_Name(String force_name) {
        this.Force_Name = force_name;
    }

    public String getForce_Status() {
        return status;
    }

    public void setSensor_Val(String status) { this.status = status; }

    public String getForce_Type() { return Force_Type; }

    public void setForce_Type(String type) {
        this.Force_Type = type;
    }
}

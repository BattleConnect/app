package com.cs495.battleconnect.responses;


/**
 * Object that holds the force object returned from the Firestore database
 */
public class ForceResponse {
    private long Date_Time;
    private double Lat;
    private double Long;
    private String Force_ID;
    private String  Force_Name;
    private String  status;
    private String  Force_Type;

    /**
     * Constructor, accepts some initial values
     * @param date_time
     * @param latitude
     * @param longitude
     * @param id
     * @param type
     * @param name
     * @param status
     */
    public ForceResponse(long date_time, double latitude, double longitude, String id, String type, String name, String status) {
        this.Date_Time = date_time;
        this.Lat = latitude;
        this.Long = longitude;
        this.Force_Name = name;
        this.status = status;
        this.Force_ID = id;
        this.Force_Type = type;

    }

    /**
     *
     * @return
     */
    public long getDate_Time() {
        return Date_Time;
    }

    /**
     *
     * @param date_time
     */
    public void setDate_Time(long date_time) { this.Date_Time = date_time; }

    /**
     *
     * @return
     */
    public double getLat() {
        return Lat;
    }

    /**
     *
     * @param latitude
     */
    public void setLat(double latitude) {
        this.Lat = latitude;
    }

    /**
     *
     * @return
     */
    public double getLong() {
        return Long;
    }

    /**
     *
     * @param longitude
     */
    public void setLong(double longitude) {
        this.Long = longitude;
    }

    /**
     *
     * @return Force_ID
     */
    public String getForce_ID() {
        return Force_ID;
    }

    /**
     *
     * @param force_id
     */
    public void setForce_ID(String force_id) {
        this.Force_ID = force_id;
    }

    /**
     *
     * @return
     */
    public String getForce_Name() { return Force_Name; }

    /**
     *
     * @param force_name
     */
    public void setForce_Name(String force_name) {
        this.Force_Name = force_name;
    }

    /**
     *
     * @return status
     */
    public String getForce_Status() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setForce_Status(String status) { this.status = status; }

    /**
     *
     * @return
     */
    public String getForce_Type() { return Force_Type; }

    /**
     *
     * @param type
     */
    public void setForce_Type(String type) {
        this.Force_Type = type;
    }
}

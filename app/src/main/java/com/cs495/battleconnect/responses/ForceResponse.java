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
     * Gets the associated timestamp.
     * @return
     */
    public long getDate_Time() {
        return Date_Time;
    }

    /**
     * Sets the timestamp.
     * @param date_time
     */
    public void setDate_Time(long date_time) { this.Date_Time = date_time; }

    /**
     * Gets the associated latitude.
     * @return
     */
    public double getLat() {
        return Lat;
    }

    /**
     * Sets the latitude.
     * @param latitude
     */
    public void setLat(double latitude) {
        this.Lat = latitude;
    }

    /**
     * Gets the associated longitude.
     * @return
     */
    public double getLong() {
        return Long;
    }

    /**
     * Sets the longitude.
     * @param longitude
     */
    public void setLong(double longitude) {
        this.Long = longitude;
    }

    /**
     * Gets the force's ID.
     * @return Force_ID
     */
    public String getForce_ID() {
        return Force_ID;
    }

    /**
     * Sets the force's ID.
     * @param force_id
     */
    public void setForce_ID(String force_id) {
        this.Force_ID = force_id;
    }

    /**
     * Gets the force's name.
     * @return
     */
    public String getForce_Name() { return Force_Name; }

    /**
     * Sets the force's name.
     * @param force_name
     */
    public void setForce_Name(String force_name) {
        this.Force_Name = force_name;
    }

    /**
     * Gets the force's status.
     * @return status
     */
    public String getForce_Status() {
        return status;
    }

    /**
     * Sets the force's status.
     * @param status
     */
    public void setForce_Status(String status) { this.status = status; }

    /**
     * Gets the force's type.
     * @return
     */
    public String getForce_Type() { return Force_Type; }

    /**
     * Sets the force's type.
     * @param type
     */
    public void setForce_Type(String type) {
        this.Force_Type = type;
    }
}

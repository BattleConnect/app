package com.cs495.battleconnect.holders.objects;

/**
 * Object used to hold force data from the firestore database
 */
public class ForceData implements java.io.Serializable {
    private String ID;
    private Long Date_Time;
    private Double Lat;
    private Double Long;
    private String Name;
    private String Status;
    private String Type;

    /**
     * Default empty constructor
     */
    public ForceData() {
    }

    /**
     * Constructor with all fields
     * @param ID
     * @param date_Time
     * @param lat
     * @param aLong
     * @param name
     * @param status
     * @param type
     */
    public ForceData(String ID, Long date_Time, Double lat, Double aLong, String name, String status, String type) {
        this.ID = ID;
        Date_Time = date_Time;
        Lat = lat;
        Long = aLong;
        Name = name;
        Status = status;
        Type = type;
    }

    /**
     * Grabs the id
     * @return
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets the id
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
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
    public void setDate_Time(java.lang.Long date_Time) {
        Date_Time = date_Time;
    }

    /**
     * Grabs the latitude
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
     * Grabs the longitude
     * @return
     */
    public Double getLong() {
        return Long;
    }

    /**
     * Sets the longitude
     * @param aLong
     */
    public void setLong(Double aLong) {
        Long = aLong;
    }

    /**
     * Grabs the force name
     * @return
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the force name
     * @param name
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Grabs the force status
     * @return
     */
    public String getStatus() {
        return Status;
    }

    /**
     * Sets the force status
     * @param status
     */
    public void setStatus(String status) {
        Status = status;
    }

    /**
     * Grabs the force type
     * @return
     */
    public String getType() {
        return Type;
    }

    /**
     * Sets the force type
     * @param type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Sets all the force data
     * @param data
     */
    public void setAll(ForceData data) {
        setDate_Time(data.getDate_Time());
        setLat(data.getLat());
        setLong(data.getLong());
        setName(data.getName());
        setStatus(data.getStatus());
        setType(data.getType());
    }
}

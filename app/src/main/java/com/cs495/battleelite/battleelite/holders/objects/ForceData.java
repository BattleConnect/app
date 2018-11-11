package com.cs495.battleelite.battleelite.holders.objects;

public class ForceData {
    private String ID;
    private Long Date_Time;
    private Double Lat;
    private Double Long;
    private String Name;
    private String Status;
    private String Type;

    public ForceData() {
    }

    public ForceData(String ID, java.lang.Long date_Time, Double lat, Double aLong, String name, String status, String type) {
        this.ID = ID;
        Date_Time = date_Time;
        Lat = lat;
        Long = aLong;
        Name = name;
        Status = status;
        Type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public java.lang.Long getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(java.lang.Long date_Time) {
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setAll(ForceData data) {
        setDate_Time(data.getDate_Time());
        setLat(data.getLat());
        setLong(data.getLong());
        setName(data.getName());
        setStatus(data.getStatus());
        setType(data.getType());
    }
}

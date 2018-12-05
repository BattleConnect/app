package com.cs495.battleconnect.holders.objects;

import com.google.android.gms.maps.model.Marker;

/**
 * Sensor marker object for the map
 */
public class SensorMarker {
    private long id;
    private String type;
    private Marker marker;

    /**
     * Constructor for sensor marker
     * @param id
     * @param type
     * @param marker
     */
    public SensorMarker(long id, String type, Marker marker) {
        this.id = id;
        this.type = type;
        this.marker = marker;
    }

    /**
     * Grab the sensor marker id
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Set the sensor marker id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Grab the sensor type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Set the sensor Type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Grab the google maps marker object
     * @return
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * Set the google maps marker object
     * @param marker
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}

package com.cs495.battleelite.battleelite.holders.objects;

import com.google.android.gms.maps.model.Marker;

/**
 * Force marker object for the map
 */
public class ForceMarker {
    private String id;
    private String type;
    private Marker marker;

    /**
     * Constructor for force marker
     * @param id
     * @param type
     * @param marker
     */
    public ForceMarker(String id, String type, Marker marker) {
        this.id = id;
        this.type = type;
        this.marker = marker;
    }

    /**
     * Grabs the force marker id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the force marker id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Grabs the force type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the force type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Grabs the google maps marker object
     * @return
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * Sets the google maps marker object
     * @param marker
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
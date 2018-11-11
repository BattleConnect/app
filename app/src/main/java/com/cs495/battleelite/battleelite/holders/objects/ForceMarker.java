package com.cs495.battleelite.battleelite.holders.objects;

import com.google.android.gms.maps.model.Marker;

public class ForceMarker {
    private String id;
    private String type;
    private Marker marker;

    public ForceMarker(String id, String type, Marker marker) {
        this.id = id;
        this.type = type;
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
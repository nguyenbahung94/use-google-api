package com.example.nbhung.mymap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/21/2017.
 */

public class LocationOfff {
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}

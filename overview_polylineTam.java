package com.example.nbhung.mymap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nbhung on 6/21/2017.
 */

public class overview_polylineTam {
    @SerializedName("points")
    private List<String> points;

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }
}

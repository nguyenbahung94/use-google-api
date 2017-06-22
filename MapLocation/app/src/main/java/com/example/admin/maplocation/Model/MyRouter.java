package com.example.admin.maplocation.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbhung on 6/21/2017.
 */

public class MyRouter {
    @SerializedName("legs")
    private List<leg> legs =new ArrayList<leg>();
    @SerializedName("overview_polyline")
    private overview_polylineTam strpolyline;

    public overview_polylineTam getStrpolyline() {
        return strpolyline;
    }

    public void setStrpolyline(overview_polylineTam strpolyline) {
        this.strpolyline = strpolyline;
    }

    public List<leg> getLegs() {
        return legs;
    }

    public void setLegs(List<leg> legs) {
        this.legs = legs;
    }


}

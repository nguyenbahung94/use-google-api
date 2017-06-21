package com.example.nbhung.mymap;

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
    private List<overview_polylineTam> listpolyline =new ArrayList<overview_polylineTam>();

    public List<overview_polylineTam> getListpolyline() {
        return listpolyline;
    }

    public void setListpolyline(List<overview_polylineTam> listpolyline) {
        this.listpolyline = listpolyline;
    }

    public List<leg> getLegs() {
        return legs;
    }

    public void setLegs(List<leg> legs) {
        this.legs = legs;
    }


}

package com.example.admin.testgrable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23/6/2017.
 */

public class InforWeather {
    @SerializedName("weather")
    private List<weather> obweather =new ArrayList<weather>();
    @SerializedName("main")
    private mainwt obmain;

    public List<weather> getObweather() {
        return obweather;
    }

    public mainwt getObmain() {
        return obmain;
    }

    @Override
    public String toString() {
        return "InforWeather{" +
                "obweather=" + obweather +
                ", obmain=" + obmain +
                '}';
    }
}

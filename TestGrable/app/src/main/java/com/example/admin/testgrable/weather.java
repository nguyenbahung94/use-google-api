package com.example.admin.testgrable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 23/6/2017.
 */

public class weather {
    @SerializedName("main")
    private String mainwt;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public String getMainwt() {
        return mainwt;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}

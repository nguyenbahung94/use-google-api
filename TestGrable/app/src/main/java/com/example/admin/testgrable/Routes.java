package com.example.admin.testgrable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbhung on 6/21/2017.
 */

public class Routes {
    @SerializedName("routes")
    private List<MyRouter> routes =new ArrayList<MyRouter>();

    public List<MyRouter> getRoutes() {
        return routes;
    }

}

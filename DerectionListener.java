package com.example.nbhung.mymap.View;

import com.example.nbhung.mymap.Model.leg;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by nbhung on 6/21/2017.
 */

public interface DerectionListener {
    void onDirecterStart();
    void onDirecterSuccess(leg leg, List<LatLng> listPoint);
}

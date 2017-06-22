package com.example.admin.maplocation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 21/6/2017.
 */

public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        int status;
        if (conn == NetworkUtil.TYPE_MOBILE || conn == NetworkUtil.TYPE_WIFI) {
            status =1;
        } else {
            status =0;
        }
        return status;
    }

}

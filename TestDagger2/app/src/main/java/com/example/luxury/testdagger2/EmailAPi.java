package com.example.luxury.testdagger2;

import android.util.Log;

import okhttp3.OkHttpClient;

/**
 * Created by Luxury on 6/28/2017.
 */

public class EmailAPi {
    OkHttpClient client;

    public EmailAPi(OkHttpClient okHttpClient) {
        client = okHttpClient;
    }

    public void sendEmail(String body, String emailURl) {
        Log.e("from: ", emailURl);
        Log.e("sent email", body);
    }
}

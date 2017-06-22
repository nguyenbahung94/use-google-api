package com.example.admin.maplocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Admin on 21/6/2017.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                int i = NetworkUtil.getConnectivityStatusString(context);
                showToast(i, context);
            }
        }.start();
    }

    public void showToast(int i, Context context) {
        if (i == 0) {
            Toast.makeText(context, "no network to connect", Toast.LENGTH_SHORT).show();
        }
//        else {
//            Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
//        }
    }
}

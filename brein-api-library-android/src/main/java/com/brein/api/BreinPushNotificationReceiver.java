package com.brein.api;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 *
 * A declaration of GcmReceiver, which handles messages sent from GCM to your application.
 * Because this service needs permission to receive messages from GCM,
 * add com.google.android.c2dm.permission.SEND to the receiver.
 *
 */
public class BreinPushNotificationReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "BrePushNotReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive invoked");

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }
}

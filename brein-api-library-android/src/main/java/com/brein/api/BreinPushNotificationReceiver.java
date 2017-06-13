package com.brein.api;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class BreinPushNotificationReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BreinPushNotificationReceiver - onReceive");

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }
        }
    }
}

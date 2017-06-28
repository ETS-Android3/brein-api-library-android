package com.brein.api;

import android.util.Log;

import com.brein.domain.BreinResult;

public class RestCallback implements ICallback {

    private static final String TAG = "RestCallback";

    @Override
    public void callback(final BreinResult data) {
        if (data != null) {
            Log.d(TAG, "callback data is:" + data.toString());
        }
    }
}

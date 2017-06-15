package com.brein.api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class BreinifyLifecycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "BreinifyLifecycle";

    private int numStarted = 0;
    private long userEnteredTime = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d(TAG, "onActivityCreated invoked");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "onActivityStarted invoked");
        if (numStarted == 0) {
            userEnteredTime = System.currentTimeMillis();

            // app is now in foreground
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "onActivityResumed invoked");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onActivityPaused invoked");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "onActivityStopped invoked");
        numStarted--;
        if (numStarted == 0) {
            long timeInApp = System.currentTimeMillis() - userEnteredTime;

            // app is now in background
            Log.d(TAG, "app is now in background");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d(TAG, "onActivitySaveInstanceState invoked");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "onActivityDestroyed invoked");
    }
}

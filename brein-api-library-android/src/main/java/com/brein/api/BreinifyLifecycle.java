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
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
        Log.d(TAG, "onActivityCreated invoked");
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        Log.d(TAG, "onActivityStarted invoked");
        if (numStarted == 0) {
            userEnteredTime = System.currentTimeMillis();

            // app is now in foreground
            BreinifyManager.getInstance().appIsInForeground();
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        Log.d(TAG, "onActivityResumed invoked");
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        Log.d(TAG, "onActivityPaused invoked");
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        Log.d(TAG, "onActivityStopped invoked");
        numStarted--;
        if (numStarted == 0) {
            long timeInApp = System.currentTimeMillis() - userEnteredTime;

            // app is now in background
            Log.d(TAG, "app is now in background. Time in App was: " + Long.toString(timeInApp));

            BreinifyManager.getInstance().appIsInBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        Log.d(TAG, "onActivitySaveInstanceState invoked");
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        Log.d(TAG, "onActivityDestroyed invoked");
    }
}

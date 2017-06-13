package com.brein.api;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by marcorecchioni on 12/06/17.
 */

public class BreinBackgroundService extends Service {

    private static final String TAG = "BreinBackgroundService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                new BreinBackgroundTask().execute();
                e.printStackTrace();
            }
            return START_STICKY;
        }
    }

    private class BreinBackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String dataToSend = params[0];

            return response;
        }

        protected void requestHttp() {
            Log.d(TAG, "within requestHttp");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    requestHttp();
                }
            }, 10000);
        }
    }

}

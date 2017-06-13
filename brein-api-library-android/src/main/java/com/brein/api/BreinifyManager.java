package com.brein.api;

import android.app.Application;
import android.content.IntentFilter;

import java.util.Map;


public class BreinifyManager {

    private static final String TAG = "BreinifyManager";

    private String pushDeviceRegistration;

    private String userEmail;

    private String userId;

    private String sessionId;

    private static volatile BreinifyManager breinifyManagerInstance;

    private Application application;

    private BreinPushNotificationReceiver breinPushNotificationReceiver = new BreinPushNotificationReceiver();

    //private constructor.
    private BreinifyManager() {

        //Prevent form the reflection api.
        if (breinifyManagerInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static BreinifyManager getInstance() {
        //Double check locking pattern
        if (breinifyManagerInstance == null) { //Check for the first time

            synchronized (BreinifyManager.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (breinifyManagerInstance == null) {
                    breinifyManagerInstance = new BreinifyManager();
                }
            }
        }
        return breinifyManagerInstance;
    }

    /**
     *
     * @param application
     * @param apiKey
     * @param secret
     */
    public void initialize(final Application application, final String apiKey, final String secret) {

        this.application = application;

        // register the callbacks for lifecycle support
        this.application.registerActivityLifecycleCallbacks(new BreinifyLifecycle());

        // configure the API
        Breinify.setConfig(apiKey, secret);

        initNotificationReceiver();
    }

    public void initNotificationReceiver() {
        final IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        application.registerReceiver(breinPushNotificationReceiver, filter);
    }

    public void destroyNotificationReceiver() {
        application.unregisterReceiver(breinPushNotificationReceiver);
    }

    public void shutdown() {
        destroyNotificationReceiver();
    }

    public void configureSesseion() {
        // Todo
    }

    public void sendLocationInformation() {
        // Todo
    }

    public void sendActivity(final String actvityType, final Map<String, Object> addtionalContent) {
        // Todo
    }

    public void sendIdentityInfo() {
        sendActivity("identity", null);
    }

    public void sendLocationInfo() {
        sendActivity("sendLoc", null);
    }

    public void readAndInitUserDefaults() {
        // TODO: 12/06/17
    }

    public void saveUserDefaults() {
        // TODO: 12/06/17
    }



}

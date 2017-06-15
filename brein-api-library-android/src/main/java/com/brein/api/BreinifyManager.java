package com.brein.api;

import android.app.Application;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.brein.util.BreinUtil;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class BreinifyManager {

    private static final String TAG = "BreinifyManager";

    private final String BREIN_PREF_NAME = "breinify.pref";

    private final String BREIN_PUSH_DEVICE_REGISTRATION = "breinPushDeviceRegistration";
    private final String BREIN_USER_EMAIL = "breinUserEmail";
    private final String BREIN_USER_ID = "breinUserId";

    private String pushDeviceRegistration;

    private String userEmail;

    private String userId;

    private String sessionId;

    private static volatile BreinifyManager breinifyManagerInstance;

    private Application application;

    private BreinPushNotificationReceiver breinPushNotificationReceiver = new BreinPushNotificationReceiver();

    // Create the Handler object (on the main thread by default)
    private Handler handler = new Handler();

    private Runnable runnableCode;

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

    public String getPushDeviceRegistration() {
        return pushDeviceRegistration;
    }

    public void setPushDeviceRegistration(final String pushDeviceRegistration) {
        this.pushDeviceRegistration = pushDeviceRegistration;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Initializes the appropriate callbacks and timer for background processing
     *
     * @param application Application contains the application object
     * @param apiKey      String contains the Breinify API Key
     * @param secret      String contains the Breinfiy secret
     */
    public void initialize(final Application application,
                           final String apiKey,
                           final String secret,
                           long backgroundInterval) {

        this.application = application;

        // register the callbacks for lifecycle support - necessary to determine if app
        // is in background or foreground
        this.application.registerActivityLifecycleCallbacks(new BreinifyLifecycle());

        // configure the API
        Breinify.setConfig(apiKey, secret);

        // configure the recipient of push notifications
        initNotificationReceiver();

        // read user defaults
        readAndInitUserDefaults();

        // configure the background processing
        initBackgroundHandler(backgroundInterval);

        // check if unsent message are there
        // TODO: 15/06/17
    }

    /**
     *
     */
    public void initNotificationReceiver() {
        final IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        application.registerReceiver(breinPushNotificationReceiver, filter);
    }

    /**
     *
     */
    public void destroyNotificationReceiver() {
        application.unregisterReceiver(breinPushNotificationReceiver);
    }

    public void initBackgroundHandler(final long backgroundInterval) {

        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("initBackgroundHandler", "Called on main thread");

                sendLocationInfo();

                // Repeat this the same runnable code block again
                handler.postDelayed(runnableCode, backgroundInterval);
            }
        };

        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }


    public void shutdown() {
        destroyNotificationReceiver();
    }

    public void configureSesseion() {
        // Todo
    }

    /**
     * send an activity
     *
     * @param activityType      contains the activity type
     * @param additionalContent can contain additional data
     */
    public void sendActivity(final String activityType,
                             final Map<String, Object> additionalContent) {

        if (BreinUtil.containsValue(this.userEmail)) {
            Breinify.getUser().setEmail(this.userEmail);
        }

        if (BreinUtil.containsValue(this.userId)) {
            Breinify.getUser().setUserId(this.userId);
        }

        if (BreinUtil.containsValue(additionalContent)) {
            Breinify.getUser().setAdditional("notification", additionalContent);
        }

        if (activityType != null) {
            Breinify.activity(activityType);
        }
    }

    /**
     *
     */
    public void sendIdentityInfo() {
        sendActivity("identity", null);
    }

    /**
     *
     */
    public void sendLocationInfo() {
        sendActivity("sendLoc", null);
    }

    /**
     *
     */
    public void readAndInitUserDefaults() {

        final SharedPreferences prefs = application.getSharedPreferences(BREIN_PREF_NAME, MODE_PRIVATE);
        if (prefs != null) {
            final String breinPushRegistration = prefs.getString(BREIN_PUSH_DEVICE_REGISTRATION, null);
            final String breinEmail = prefs.getString(BREIN_USER_EMAIL, null);
            final String breinUserId = prefs.getString(BREIN_USER_ID, null);

            if (BreinUtil.containsValue(breinUserId)) {
                Breinify.setUserId(breinUserId);
            }

            if (BreinUtil.containsValue(breinEmail)) {
                Breinify.setEmail(breinEmail);
            }

            if (BreinUtil.containsValue(breinPushRegistration)) {
                Breinify.setPushDeviceRegistration(breinPushRegistration);
            }
        }
    }

    public void saveUserDefaults() {
        // TODO: 12/06/17
    }

    /**
     * @param deviceToken
     */
    public void configureDeviceToken(final String deviceToken) {
        Breinify.getUser().setPushDeviceRegistration(deviceToken);

        // save device registration
        saveUserDefaultValue(BREIN_PUSH_DEVICE_REGISTRATION, deviceToken);

        sendIdentityInfo();
    }

    /**
     * @param key
     * @param value
     */
    public void saveUserDefaultValue(final String key, final String value) {
        final SharedPreferences.Editor editor = application.getSharedPreferences(BREIN_PREF_NAME, MODE_PRIVATE).edit();
        if (editor != null) {
            editor.putString(key, value);
            editor.apply();
        }
    }
}

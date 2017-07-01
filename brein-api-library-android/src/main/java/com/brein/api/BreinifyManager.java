package com.brein.api;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.brein.util.BreinUtil;

import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class BreinifyManager {

    // some handy constants
    private static final String TAG = "BreinifyManager";
    private static final String BREIN_PREF_NAME = "breinify.pref";
    private static final String BREIN_PUSH_DEVICE_REGISTRATION = "breinPushDeviceRegistration";
    private static final String BREIN_USER_EMAIL = "breinUserEmail";
    private static final String BREIN_USER_ID = "breinUserId";

    // contains the device token
    private String pushDeviceRegistration;

    // contains the user email
    private String userEmail;

    // contains the user id
    private String userId;

    // contains the sesionid
    private String sessionId;

    // used for singleton
    private static volatile BreinifyManager breinifyManagerInstance;

    // application context
    private Application application;

    // contains the main activity
    private Activity mainActivity;

    // instance of push notification service
    private final BreinPushNotificationReceiver breinPushNotificationReceiver = new BreinPushNotificationReceiver();

    // Create the Handler object (on the main thread by default)
    private final Handler handler = new Handler();

    private Runnable runnableCode;
    private String apiKey;
    private String secret;
    private long backgroundInterval = 0;

    // private constructor
    private BreinifyManager() {

        // Prevent form the reflection api.
        if (breinifyManagerInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    /**
     * Singleton mechanism
     *
     * @return instance of BreinifyManager
     */
    public static BreinifyManager getInstance() {
        // Double check locking pattern
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
     * Returns the deviceRegistration
     *
     * @return String
     */
    public String getPushDeviceRegistration() {
        return pushDeviceRegistration;
    }

    /**
     * Set the deviceRegistration
     *
     * @param pushDeviceRegistration String
     */
    public void setPushDeviceRegistration(final String pushDeviceRegistration) {
        Log.d(TAG, "pushDeviceRegistration is: " + pushDeviceRegistration);
        this.pushDeviceRegistration = pushDeviceRegistration;

        // set user as well -> necessary for correct request
        Breinify.getUser().setPushDeviceRegistration(pushDeviceRegistration);
    }

    /**
     * @return
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     */
    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     */
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Provides the Android Application Object
     *
     * @return Android Application
     */
    public Application getApplication() {
        return application;
    }

    /**
     *
     * @return
     */
    public Activity getMainActivity() {
        return mainActivity;
    }

    /**
     *
     * @param mainActivity
     */
    public void setMainActivity(final Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Sets the Android Application Object
     *
     * @param application Android Application Object
     */
    public void setApplication(final Application application) {
        this.application = application;
    }

    /**
     * Initializes the appropriate callbacks and timer for background processing
     *
     * @param application Application contains the application object
     * @param mainActivity
     * @param apiKey      String contains the Breinify API Key
     * @param secret      String contains the Breinfiy secret
     */
    public void initialize(final Application application,
                           final Activity mainActivity,
                           final String apiKey,
                           final String secret,
                           long backgroundInterval) {

        this.application = application;
        this.mainActivity = mainActivity;
        this.apiKey = apiKey;
        this.secret = secret;
        this.backgroundInterval = backgroundInterval;

        initLifecycleAndEngine(backgroundInterval);
    }

    /**
     * Initializes the Lifecycle support and Engine
     *
     * @param backgroundInterval
     */
    private void initLifecycleAndEngine(long backgroundInterval) {
        // register the callbacks for lifecycle support - necessary to determine if app
        // is in background or foreground

        if (this.application != null) {
            this.application.registerActivityLifecycleCallbacks(new BreinifyLifecycle());
        }

        // configure the API
        Breinify.setConfig(this.apiKey, this.secret);

        // configure the recipient of push notifications
        initNotificationReceiver();

        // read user defaults (email, userId, token)
        readAndInitUserDefaults();

        // configure the background processing
        initBackgroundHandler(backgroundInterval);

        // configure the session
        configureSession();

        // send the user identification to the backend
        sendIdentifyInfo();

        // check if unsent message are there
        // TODO: 15/06/17
    }

    /**
     * Initializes the notification receiver programmatically
     */
    public void initNotificationReceiver() {
        if (application != null) {
            final IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
            application.registerReceiver(breinPushNotificationReceiver, filter);
        }
    }

    /**
     *
     */
    public void destroyNotificationReceiver() {
        if (application != null) {
            application.unregisterReceiver(breinPushNotificationReceiver);
        }
    }

    /**
     * Background Handler for sending messages
     *
     * @param backgroundInterval interval in ms
     */
    public void initBackgroundHandler(final long backgroundInterval) {

        Log.d(TAG, "initBackgroundHandler invoked with duration: " + Long.toString(backgroundInterval));

        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("initBackgroundHandler", "Called on main thread");

                // flag if sending is possible
                sendLocationInfo();

                // Repeat this the same runnable code block again
                handler.postDelayed(runnableCode, backgroundInterval);
            }
        };

        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    /**
     * Called in case of API Shutdown and will stop all services
     */
    public void shutdown() {
        Log.d(TAG, "shutdown invoked ");

        destroyNotificationReceiver();
    }

    /**
     * initializes the session id with an identifier
     */
    public void configureSession() {
        setSessionId(UUID.randomUUID().toString());
    }

    /**
     * send an activity
     *
     * @param activityType      contains the activity type
     * @param additionalContent can contain additional data
     */
    public void sendActivity(final String activityType,
                             final Map<String, Object> additionalContent) {

        Log.d(TAG, "sending activity of type: " + activityType);

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
     * send an identify information ony if token is given
     */
    public void sendIdentifyInfo() {
        Log.d(TAG, "sendIdentifyInfo invoked");

        if (BreinUtil.containsValue(BreinifyManager.getInstance().getPushDeviceRegistration())) {
            sendActivity("identify", null);
        }
    }

    /**
     * send a location information
     */
    public void sendLocationInfo() {
        Log.d(TAG, "sendLocationInfo invoked");

        if (application == null) {
            Log.d(TAG, "sendLocationInfo. Can not check permissions because application object not set");
            return;
        }

        if (!BreinUtil.containsValue(BreinifyManager.getInstance().getPushDeviceRegistration())) {
            Log.d(TAG, "sendLocationInfo. No deviceRegistrationToken set.");
            return;
        }

        final int accessFineLocationPermission = ActivityCompat.checkSelfPermission(application,
                Manifest.permission.ACCESS_FINE_LOCATION);

        final int accessCoarseLocationPermission = ActivityCompat.checkSelfPermission(application,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (accessCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ||
                accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "no permission granted to invoke location requests");
            return;
        }

        sendActivity("sendLoc", null);
    }

    /**
     * read the user defaults and initializes the brein email and userId properties
     */
    public void readAndInitUserDefaults() {

        Log.d(TAG, "readAndInitUserDefaults invoked");

        if (application == null) {
            Log.d(TAG, "readAndInitUserDefaults can not work, because application object not set.");
        }

        final SharedPreferences prefs = application.getSharedPreferences(BREIN_PREF_NAME, MODE_PRIVATE);
        if (prefs != null) {

            final String breinPushRegistration = prefs.getString(BREIN_PUSH_DEVICE_REGISTRATION, null);
            final String breinEmail = prefs.getString(BREIN_USER_EMAIL, null);

            // generate UUID if not already generated
            final String breinUserId = prefs.getString(BREIN_USER_ID, UUID.randomUUID().toString());

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

    /**
     * Save email and userId in user defaults and send
     * an identifyInfo to the backend.
     */
    public void saveUserDefaults() {
        Log.d(TAG, "saveUserDefaults invoked");

        saveUserDefaultValue(BREIN_USER_EMAIL, getUserEmail());
        saveUserDefaultValue(BREIN_USER_ID, getUserId());

        // in case of changed user-identification
        sendIdentifyInfo();
    }

    /**
     * Save the device token
     *
     * @param deviceToken
     */
    public void configureDeviceToken(final String deviceToken) {
        Log.d(TAG, "configureDeviceToken invoked wit token: " + deviceToken);

        setPushDeviceRegistration(deviceToken);

        // save device registration
        saveUserDefaultValue(BREIN_PUSH_DEVICE_REGISTRATION, deviceToken);

        // send Identify
        sendIdentifyInfo();
    }

    /**
     * Helper to save key - value pair as user defaults
     *
     * @param key   contains the key
     * @param value contains the value
     */
    public void saveUserDefaultValue(final String key, final String value) {
        if (application != null) {
            final SharedPreferences.Editor editor = application.getSharedPreferences(BREIN_PREF_NAME, MODE_PRIVATE).edit();
            if (editor != null) {
                if (key != null && value != null) {
                    editor.putString(key, value);
                    editor.apply();
                }
            }
        }
    }

    /**
     * Invoked when app is set to foreground, set saved sessionId
     */
    public void appIsInForeground() {
        Breinify.getUser().setSessionId(this.sessionId);
    }

    /**
     * Invoked when app is in set to background, remove sessionId
     */
    public void appIsInBackground() {
        Breinify.getUser().setSessionId("");
    }
}

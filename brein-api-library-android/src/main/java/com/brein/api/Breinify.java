package com.brein.api;

import android.app.Activity;
import android.app.Application;

import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;

import java.util.Map;

/**
 * Static Implementation of Breinify activity  lookup calls
 */
public class Breinify {

    private static BreinConfig lastConfig = null;
    private static Brein lastBrein = null;

    private static final BreinUser breinUser = new BreinUser();

    /**
     * contains the current version of the usage library
     */
    private static final String VERSION = BreinConfig.VERSION;

    /**
     * contains the activity object
     */
    private static final BreinActivity breinActivity = new BreinActivity();

    /**
     * contains the lookup object
     */
    private static final BreinLookup breinLookup = new BreinLookup();

    /**
     * contains the temporalData object
     */
    // private static final BreinTemporalData breinTemporalData = new BreinTemporalData();

    /**
     * Specifies the overall configuration used by the library. The configuration must be set prior to any call to the
     * API.
     *
     * @param config the configuration to use
     * @return the {@code Brein} instance, usable if multiple different configurations are used
     * @see Brein
     * @see BreinConfig
     */
    public static Brein setConfig(final BreinConfig config) {
        lastConfig = config;
        return new Brein().setConfig(config);
    }

    /**
     * Specifies the overall configuration used by the library. The configuration must be set prior to any call to the
     * API.
     *
     * @param apiKey the API key to be used
     * @return the {@code Brein} instance, usable if multiple different configurations are used
     * @see Brein
     */
    public static Brein setConfig(final String apiKey) {
        return setConfig(apiKey, null);
    }

    /**
     * Specifies the overall configuration used by the library. The configuration must be set prior to any call to the
     * API.
     *
     * @param apiKey the API key to be used
     * @param secret the secret to be used to sign the messages (Verification Signature must be enabled for the API
     *               key)
     * @return the {@code Brein} instance, usable if multiple different configurations are used
     * @see Brein
     */
    public static Brein setConfig(final String apiKey, final String secret) {
        return setConfig(new BreinConfig(apiKey, secret));
    }

    /**
     * Initializes the instance
     *
     * @param application contains the application context
     * @param mainActivity contains the main activity
     * @param apiKey contains the apiKey
     * @param secret contains the secret
     */
    public static void initialize(final Application application,
                                  final Activity mainActivity,
                                  final String apiKey,
                                  final String secret) {
        final long backgroundTimeInMS = 60 * (long)1000;

        initialize(application, mainActivity, apiKey, secret, backgroundTimeInMS);
    }

    /**
     * Initializes the instance
     *
     * @param application contains the application context
     * @param mainActivity contains the main activity
     * @param apiKey contains the apiKey
     * @param secret contains the secret
     * @param backgroundInterval sets a background interval
     */
    public static void initialize(final Application application,
                                  final Activity mainActivity,
                                  final String apiKey,
                                  final String secret,
                                  final long backgroundInterval) {
        BreinifyManager.getInstance().initialize(application,
                mainActivity,
                apiKey,
                secret,
                backgroundInterval);
    }

    /**
     * configures the deviceToken
     *
     * @param deviceToken contains the deviceToken
     */
    public static void configureDeviceToken(final String deviceToken) {
        BreinifyManager.getInstance().configureDeviceToken(deviceToken);
    }

    /**
     * gets the config
     *
     * @return config
     */
    public static BreinConfig getConfig() {
        return lastConfig;
    }

    /**
     * returns the  version
     *
     * @return version
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * @return breinActivity instance
     */
    public static BreinActivity getBreinActivity() {
        return breinActivity;
    }

    /**
     * @return breinLookup instance
     */
    public static BreinLookup getBreinLookup() {
        return breinLookup;
    }

    public static BreinUser getUser() {
        return breinUser;
    }

    /**
     * Service method to set the email property that is part of the
     * BreinifyManager instance
     * @param email contains the email
     */
    public static void setEmail(final String email) {
        BreinifyManager.getInstance().setUserEmail(email);
    }

    /**
     * Service method to set the userId property that is part of the
     * BreinifyManager instance
     * @param userId contaisns the userId
     */
    public static void setUserId(final String userId) {
        BreinifyManager.getInstance().setUserId(userId);
    }

    /**
     * returns user's email as part of BreinifyManager
     * @return email
     */
    public static String getEmail() {
        return BreinifyManager.getInstance().getUserEmail();
    }

    /**
     * returns userId as part of BreinifyManager
     * @return userId
     */
    public static String getUserId() {
        return BreinifyManager.getInstance().getUserId();
    }

    /**
     * sets the pushDeviceToken
     * @param token contains the token
     */
    public static void setPushDeviceRegistration(final String token) {
        BreinifyManager.getInstance().setPushDeviceRegistration(token);
    }

    /**
     * Delegate to save userDefaults
     */
    public static void saveUserDefaults() {
        BreinifyManager.getInstance().saveUserDefaults();
    }

    /**
     * Delegate to send activities
     * @param activityType contains the type of activity
     * @param additionalContent contains optional additional content
     */
    public static void sendActivity(final String activityType,
                                    final Map<String, Object> additionalContent) {
        BreinifyManager.getInstance().sendActivity(activityType, null);
    }

    /**
     * Sends an activity to the engine utilizing the API. The call is done asynchronously as a POST request. It is
     * important that a valid API-key is configured prior to using this function.
     * <p>
     * This request is asynchronous.
     *
     * @param user         a plain object specifying the user information the activity belongs to
     * @param activityType the type of the activity collected, i.e., one of search, login, logout, addToCart,
     *                     removeFromCart, checkOut, selectProduct, or other. if not specified, the default other will
     *                     be used
     * @param category     the category of the platform/service/products, i.e., one of apparel, home, education, family,
     *                     food, health, job, services, or other
     * @param description  a string with further information about the activity performed
     */
    public static void activity(final BreinUser user,
                                final String activityType,
                                final String category,
                                final String description,
                                final ICallback<BreinResult> callback) {

        if (user == null) {
            throw new BreinException(BreinException.USER_NOT_SET);
        }

        final BreinActivity activity = new BreinActivity()
                .setUser(user)
                .setActivityType(activityType)
                .setCategory(category)
                .setDescription(description);

        activity(activity, callback);
    }

    /**
     * Sends an activity to the engine utilizing the API. The call is done asynchronously as a POST request. It is
     * important that a valid API-key is configured prior to using this function.
     * <p>
     * This request is asynchronous.
     *
     * @param user         a plain object specifying the user information the activity belongs to
     * @param activityType the type of the activity collected, i.e., one of search, login, logout, addToCart,
     *                     removeFromCart, checkOut, selectProduct, or other. if not specified, the default other will
     *                     be used
     * @param category     the category of the platform/service/products, i.e., one of apparel, home, education, family,
     *                     food, health, job, services, or other
     * @param description  a string with further information about the activity performed
     */
    public static void activity(final BreinUser user,
                                final String activityType,
                                final String category,
                                final String description) {

        // invoke activity call without callback
        activity(user, activityType, category, description, null);
    }

    /**
     * Method to send an activity asynchronous.
     *
     * @param activityType the activty type to be sent
     * @see BreinActivity
     */
    public static void activity(final String activityType) {

        breinActivity.setUser(breinUser);
        breinActivity.setActivityType(activityType);

        activity(breinActivity, null);
    }

    /**
     * Method to send an activity asynchronous.
     *
     * @param activity the {@code BreinActivity} to be sent
     * @see BreinActivity
     */
    public static void activity(final BreinActivity activity) {
        activity(activity, null);
    }

    /**
     * Method to send an activity asynchronous.
     *
     * @param activity the {@code BreinActivity} to be sent
     * @param callback callback to get informed whenever the activity was sent, the callback retrieves the {@code
     *                 BreinResult}
     * @see BreinActivity
     * @see BreinResult
     */
    public static void activity(final BreinActivity activity, final ICallback<BreinResult> callback) {
        getBrein().activity(activity, callback);
    }

    /**
     * Method to retrieve temporal information based on temporal data. This method uses the available information from
     * the system it is running on to be passed to the API, which resolves the temporal information. Normally (if not
     * using a VPN) the ip-address is a good source to determine, e.g., the location.
     */
    public static void temporalData(final ICallback<BreinResult> callback) {

        final BreinTemporalData data = new BreinTemporalData().setLocalDateTime();
        temporalData(data, callback);
    }

    /**
     * Method to retrieve temporal information based on temporal data. This method uses the {@code latitude} and {@code
     * longitude} to determine further information, i.e., weather, location, events, time, timezone, and holidays.
     *
     * @param latitude   the latitude of the geo-coordinates to resolve
     * @param longitude  the longitude of the geo-coordinates to resolve
     * @param shapeTypes the shape-types to retrieve (if empty, no shape-types will be returned), e.g., CITY,
     *                   NEIGHBORHOOD, ZIP-CODES
     */
    public static void temporalData(final double latitude,
                                    final double longitude,
                                    final ICallback<BreinResult> callback,
                                    final String... shapeTypes) {
        final BreinTemporalData data = new BreinTemporalData()
                .setLongitude(longitude)
                .setLatitude(latitude)
                .addShapeTypes(shapeTypes);

        getBrein().temporalData(data, callback);
    }

    /**
     * Method to retrieve temporal information based on temporal data. This method uses the {@code ipAddress} to
     * determine further information, i.e., weather, location, events, time, timezone, and holidays.
     *
     * @param ipAddress the address to resolve the information for
     */
    public static void temporalData(final String ipAddress, final ICallback<BreinResult> callback) {
        final BreinTemporalData data = new BreinTemporalData().setLookUpIpAddress(ipAddress);

        getBrein().temporalData(data, callback);
    }

    /**
     * Method to retrieve temporal information based on temporal data. This method uses the available information from
     * the system it is running on to be passed to the API, which resolves the temporal information. Normally (if not
     * using a VPN) the ip-address is a good source to determine, e.g., the location.
     */
    public static void temporalData(final BreinTemporalData data, final ICallback<BreinResult> callback) {
        getBrein().temporalData(data, callback);
    }

    /**
     * Invokes recommendation request
     *
     * @param data BreinRecommendation instance
     * @param callback ICallback constains callback handler
     */
    public static void recommendation(final BreinRecommendation data, final ICallback<BreinResult> callback) {
        getBrein().recommendation(data, callback);
    }

    /**
     * Retrieves a lookup result from the engine. The function needs a valid API-key to be configured to succeed.
     * <p>
     * This request is synchronous.
     *
     * @param data     a plain object specifying information about the brein lookup data.
     * @param callback a method invoked with the result set.
     */
    public static void lookUp(final BreinLookup data, final ICallback<BreinResult> callback) {
        getBrein().lookup(data, callback);
    }

    /**
     *
     * @return
     */
    protected static Brein getBrein() {
        if (lastBrein == null) {
            lastBrein = new Brein().setConfig(lastConfig);
        }
        return lastBrein;
    }

    /**
     * Shutdown Breinify services
     */
    public static void shutdown() {
        if (getConfig() != null) {
            getConfig().shutdownEngine();
        }

        BreinifyManager.getInstance().shutdown();
    }
}

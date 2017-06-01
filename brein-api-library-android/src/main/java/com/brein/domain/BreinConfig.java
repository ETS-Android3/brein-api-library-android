package com.brein.domain;

import android.content.Context;

import com.brein.api.BreinInvalidConfigurationException;
import com.brein.engine.BreinEngine;
import com.brein.engine.BreinEngineType;
import com.brein.util.BreinUtil;

/**
 * Contains Breinify Endpoint configuration
 */
public class BreinConfig {

    /**
     * contains the current version of the usage library
     */
    public static final String VERSION = "1.0.0";

    /**
     * default endpoint of activity
     */
    public static final String DEFAULT_ACTIVITY_ENDPOINT = "/activity";

    /**
     * default endpoint for temporalData
     */
    public static final String DEFAULT_TEMPORALDATA_ENDPOINT = "/temporaldata";

    /**
     * default endpoint for recommendation
     */
    public static final String DEFAULT_RECOMMENDATION_ENDPOINT = "/recommendation";

    /**
     * default endpoint of lookup
     */
    public static final String DEFAULT_LOOKUP_ENDPOINT = "/lookup";

    /**
     * default connection timeout in ms
     */
    public static final long DEFAULT_CONNECTION_TIMEOUT = 10000;

    /**
     * default socket timeout in ms
     */
    public static final long DEFAULT_SOCKET_TIMEOUT = 10000;

    /**
     * default breinify base url
     */
    public static final String DEFAULT_BASE_URL = "https://api.breinify.com";

    /**
     * BASE URL
     */
    private String baseUrl = DEFAULT_BASE_URL;

    /**
     * contains the api-key
     */
    private String apiKey;

    /**
     * Default REST client
     */
    private BreinEngineType restEngineType = BreinEngineType.HTTP_URL_CONNECTION_ENGINE;

    /**
     * contains the activity endpoint (default = ACTIVITY_ENDPOINT)
     */
    private String activityEndpoint = DEFAULT_ACTIVITY_ENDPOINT;

    /**
     * contains the lookup endpoint (default = LOOKUP_ENDPOINT)
     */
    private String lookupEndpoint = DEFAULT_LOOKUP_ENDPOINT;

    /**
     * contains the temporalData endpoint (default = DEFAULT_TEMPORALDATA_ENDPOINT)
     */
    private String temporalDataEndpoint = DEFAULT_TEMPORALDATA_ENDPOINT;

    /**
     * contains the recommendation endpoint (default = DEFAULT_RECOMMENDATION_ENDPOINT)
     */
    private String recommendationEndpoint = DEFAULT_RECOMMENDATION_ENDPOINT;

    /**
     * connection timeout
     */
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    /**
     * Engine with default value
     */
    private BreinEngine breinEngine;

    /**
     * socket timeout
     */
    private long socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    /**
     * default category
     */
    private String defaultCategory = "";

    /**
     * contains the secret
     */
    private String secret;

    /**
     * contains the android application applicationContext
     */
    private Context applicationContext;

    /**
     * @param apiKey  contains the Breinify com.brein.api-key
     */
    public BreinConfig(final String apiKey) {

        setApiKey(apiKey);
        if (this.getRestEngineType() != BreinEngineType.HTTP_URL_CONNECTION_ENGINE) {
            setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE);
        }
    }

    /**
     * @param apiKey  contains the Breinify com.brein.api-key
     * @param secret contains the secret
     */
    public BreinConfig(final String apiKey,
                       final String secret) {

        this(apiKey);
        setSecret(secret);
        initEngine();
    }

    /**
     * Configuration object
     *
     * @param apiKey            contains the Breinify com.brein.api-key
     * @param secret            contains the secret
     * @param breinEngineType   selected com.brein.engine
     */
    public BreinConfig(final String apiKey,
                       final String secret,
                       final BreinEngineType breinEngineType) {

        this(apiKey);
        setSecret(secret);
        setRestEngineType(breinEngineType);
        initEngine();
    }

    /**
     * Empty Ctor - necessary
     */
    public BreinConfig() {
    }

    /**
     * initializes the rest client
     */
    public void initEngine() {
        breinEngine = new BreinEngine();
    }

    /**
     * retrieves the base url
     *
     * @return base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * set the base url of the breinify backend and will check
     * if the URL is valid.
     *
     * @param baseUrl contains the url
     * @return the config object itself
     */
    public BreinConfig setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
        checkBaseUrl(baseUrl);
        return this;
    }

    /**
     * checks if the url is valid. If not a BreinInvalidConfigurationException will
     * be thrown.
     *
     * @param baseUrl url to check
     */
    public void checkBaseUrl(final String baseUrl) throws BreinInvalidConfigurationException {

        if (!isUrlValid(baseUrl)) {
            final String msg = "BreinConfig issue. Value for BaseUrl is not valid. Value is: "
                    + baseUrl;
            // Log.d("BreinUtil", msg);

            throw new BreinInvalidConfigurationException(msg);
        }
    }

    /**
     * retrieves rest type client
     *
     * @return configured rest type client
     */
    public BreinEngineType getRestEngineType() {
        return restEngineType;
    }

    /**
     * set rest type client
     *
     * @param restEngineType of the rest impl
     * @return the config object itself
     */
    public BreinConfig setRestEngineType(final BreinEngineType restEngineType) {
        this.restEngineType = restEngineType;
        return this;
    }

    /**
     * returns the configured brein com.brein.engine for the rest calls
     *
     * @return brein com.brein.engine
     */
    public BreinEngine getBreinEngine() {
        return breinEngine;
    }

    /**
     * retrieves the apikey
     *
     * @return apikey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * sets the apikey
     *
     * @param apiKey the apikey
     * @return the config object itself
     */
    public BreinConfig setApiKey(final String apiKey) {

        if (BreinUtil.containsValue(apiKey)) {
            this.apiKey = apiKey;
        }
        return this;
    }

    /**
     * retrieves the url for the post requests
     *
     * @return base url
     */
    public String getUrl() {
        return baseUrl;
    }

    /**
     * retrieves the configured timeout values
     *
     * @return connection time out
     */
    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * set the connection timeout
     *
     * @param connectionTimeout value
     */
    public void setConnectionTimeout(final long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * socket timeout values
     *
     * @return connection time out values
     */
    public long getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * set the socket timeout
     *
     * @param socketTimeout value
     */
    public void setSocketTimeout(final long socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * retrieves the activity endpoint
     *
     * @return endpoint
     */
    public String getActivityEndpoint() {
        return activityEndpoint;
    }

    /**
     * sets the activity endpoint
     *
     * @param activityEndpoint endpoint
     * @return the config object itself
     */
    public BreinConfig setActivityEndpoint(final String activityEndpoint) {
        this.activityEndpoint = activityEndpoint;
        return this;
    }

    /**
     * retrieves the lookup endpoint
     *
     * @return lookup endpoint
     */
    public String getLookupEndpoint() {
        return lookupEndpoint;
    }

    /**
     * sets the lookup endpoint
     *
     * @param lookupEndpoint endpoint
     * @return the config object itself
     */
    public BreinConfig setLookupEndpoint(final String lookupEndpoint) {
        this.lookupEndpoint = lookupEndpoint;
        return this;
    }

    /**
     * retrieves the temporalData endpoint
     *
     * @return temporalData endpoint
     */
    public String getTemporalDataEndpoint() {
        return this.temporalDataEndpoint;
    }

    /**
     * sets the temporalData endpoint
     *
     * @param temporalDataEndpoint endpoint
     * @return the config object itself
     */
    public void setTemporalDataEndpoint(final String temporalDataEndpoint) {
        this.temporalDataEndpoint = temporalDataEndpoint;
    }

    /**
     * retrieves the recommendation endpoint
     *
     * @return recommendationEndpoint endpoint
     */
    public String getRecommendationEndpoint() {
        return this.recommendationEndpoint;
    }

    /**
     * sets the recommendation endpoint
     *
     * @param recommendationEndpoint endpoint
     * @return the config object itself
     */
    public BreinConfig setRecommendationEndpoint(final String recommendationEndpoint) {
        this.recommendationEndpoint = recommendationEndpoint;
        return this;
    }

    /**
     * returns the configured secret
     *
     * @return raw secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * set the secret
     *
     * @param secret raw secret
     */
    public BreinConfig setSecret(final String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * returns the default category (if set)
     *
     * @return default category
     */
    public String getDefaultCategory() {
        return defaultCategory;
    }

    /**
     * sets the default category
     *
     * @param defaultCategory default to set
     */
    public BreinConfig setDefaultCategory(final String defaultCategory) {
        this.defaultCategory = defaultCategory;
        return this;
    }

    /**
     * @return
     */
    public Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param context
     */
    public void setApplicationContext(final Context context) {
        this.applicationContext = context;
    }

    /**
     * invokes the termination of the rest com.brein.engine.
     * Depending of the configured com.brein.engine additional threads might
     * have been allocated and this will close those threads.
     */
    // TODO: check if this is the right point
    public void shutdownEngine() {

        // check valid objects
        if (this.breinEngine == null) {
            return;
        }

        if (this.breinEngine.getRestEngine() == null) {
            return;
        }

        // invoke termination of the com.brein.engine
        this.breinEngine.getRestEngine().terminate();
    }

    /**
     * Validates if the URL is correct.
     *
     * @param url to check
     * @return true if ok otherwise false
     */
    public boolean isUrlValid(final String url) {
        return BreinUtil.isUrlValid(url);
    }

    public boolean isSign() {
        return getSecret() != null && !getSecret().isEmpty();
    }

}

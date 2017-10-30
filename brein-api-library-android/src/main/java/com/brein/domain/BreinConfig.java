package com.brein.domain;

import android.app.Application;

import com.brein.api.BreinInvalidConfigurationException;
import com.brein.api.BreinifyManager;
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
    public static final String VERSION = "1.0.2";

    /**
     * default endpoint of activity
     */
    private static final String DEFAULT_ACTIVITY_ENDPOINT = "/activity";

    /**
     * default endpoint for temporalData
     */
    private static final String DEFAULT_TEMPORALDATA_ENDPOINT = "/temporaldata";

    /**
     * default endpoint for recommendation
     */
    private static final String DEFAULT_RECOMMENDATION_ENDPOINT = "/recommendation";

    /**
     * default endpoint of lookup
     */
    private static final String DEFAULT_LOOKUP_ENDPOINT = "/lookup";

    /**
     * default connection timeout in ms
     */
    private static final long DEFAULT_CONNECTION_TIMEOUT = 10000;

    /**
     * default socket timeout in ms
     */
    private static final long DEFAULT_SOCKET_TIMEOUT = 10000;

    /**
     * default breinify base url
     */
    private static final String DEFAULT_BASE_URL = "https://api.breinify.com";

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
     * contains the android application application
     */
    private Application application;

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
     * @param apiKey  String contains the Breinify com.brein.api-key
     * @param secret  String contains the secret
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
     * @param apiKey            String contains the Breinify com.brein.api-key
     * @param secret            String contains the secret
     * @param breinEngineType   BreinEngineType selected com.brein.engine
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
     * @return  String base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * set the base url of the breinify backend and will check
     * if the URL is valid.
     *
     * @param baseUrl  String contains the url
     * @return         BreinConfig the config object itself
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
     * @param baseUrl String url to check
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
     * @return  BreinEngineType configured rest type client
     */
    public BreinEngineType getRestEngineType() {
        return restEngineType;
    }

    /**
     * set rest type client
     *
     * @param restEngineType  BreinEngineType rest impl
     * @return                BreinConfig the config object itself
     */
    public BreinConfig setRestEngineType(final BreinEngineType restEngineType) {
        this.restEngineType = restEngineType;
        return this;
    }

    /**
     * returns the configured BreinEngine for the rest calls
     *
     * @return   BreinEngine
     */
    public BreinEngine getBreinEngine() {
        return breinEngine;
    }

    /**
     * retrieves the apikey
     *
     * @return  String apikey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * sets the apikey
     *
     * @param apiKey   String the apikey
     * @return         BreinConfig the config object itself
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
     * @return String base url
     */
    public String getUrl() {
        return baseUrl;
    }

    /**
     * retrieves the configured timeout values
     *
     * @return long connection time out
     */
    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * set the connection timeout
     *
     * @param connectionTimeout long value
     */
    public void setConnectionTimeout(final long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * socket timeout values
     *
     * @return long connection time out values
     */
    public long getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * set the socket timeout
     *
     * @param socketTimeout long value
     */
    public void setSocketTimeout(final long socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * retrieves the activity endpoint
     *
     * @return String endpoint
     */
    public String getActivityEndpoint() {
        return activityEndpoint;
    }

    /**
     * sets the activity endpoint
     *
     * @param activityEndpoint String endpoint
     * @return                 BreinConfig the config object itself
     */
    public BreinConfig setActivityEndpoint(final String activityEndpoint) {
        this.activityEndpoint = activityEndpoint;
        return this;
    }

    /**
     * retrieves the lookup endpoint
     *
     * @return String lookup endpoint
     */
    public String getLookupEndpoint() {
        return lookupEndpoint;
    }

    /**
     * sets the lookup endpoint
     *
     * @param lookupEndpoint String endpoint
     * @return               BreinConfig the config object itself
     */
    public BreinConfig setLookupEndpoint(final String lookupEndpoint) {
        this.lookupEndpoint = lookupEndpoint;
        return this;
    }

    /**
     * retrieves the temporalData endpoint
     *
     * @return String temporalData endpoint
     */
    public String getTemporalDataEndpoint() {
        return this.temporalDataEndpoint;
    }

    /**
     * sets the temporalData endpoint
     *
     * @param temporalDataEndpoint String endpoint
     */
    public void setTemporalDataEndpoint(final String temporalDataEndpoint) {
        this.temporalDataEndpoint = temporalDataEndpoint;
    }

    /**
     * retrieves the recommendation endpoint
     *
     * @return String recommendationEndpoint endpoint
     */
    public String getRecommendationEndpoint() {
        return this.recommendationEndpoint;
    }

    /**
     * sets the recommendation endpoint
     *
     * @param recommendationEndpoint String endpoint
     * @return                       BreinConfig the config object itself
     */
    public BreinConfig setRecommendationEndpoint(final String recommendationEndpoint) {
        this.recommendationEndpoint = recommendationEndpoint;
        return this;
    }

    /**
     * returns the configured secret
     *
     * @return String raw secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * set the secret
     *
     * @param secret String raw secret
     * @return       BreinConfig the config object itself
     */
    public BreinConfig setSecret(final String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * returns the default category (if set)
     *
     * @return String default category
     */
    public String getDefaultCategory() {
        return defaultCategory;
    }

    /**
     * sets the default category
     *
     * @param defaultCategory String default to set
     */
    public BreinConfig setDefaultCategory(final String defaultCategory) {
        this.defaultCategory = defaultCategory;
        return this;
    }

    /**
     * Sets the Android Application
     * @param application  Application instance
     * @return             BreinConfig the object itself
     */
    public BreinConfig setApplication(final Application application) {
        this.application = application;
        return this;
    }

    /**
     * Provides the Android Application Object
     *
     * @return Application Android Application Object
     */
    public Application getApplication() {
        return BreinifyManager.getInstance().getApplication();
    }

    /**
     * invokes the termination of the rest com.brein.engine.
     * Depending of the configured com.brein.engine additional threads might
     * have been allocated and this will close those threads.
     */
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
     * @param url String to check
     * @return boolean true if ok otherwise false
     */
    public boolean isUrlValid(final String url) {
        return BreinUtil.isUrlValid(url);
    }

    public boolean isSign() {
        return getSecret() != null && !getSecret().isEmpty();
    }

}

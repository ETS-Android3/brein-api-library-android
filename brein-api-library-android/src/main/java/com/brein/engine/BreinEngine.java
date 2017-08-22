package com.brein.engine;

import com.brein.api.BreinActivity;
import com.brein.api.BreinBase;
import com.brein.api.BreinLookup;
import com.brein.api.ICallback;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;

import static com.brein.engine.BreinEngineType.HTTP_URL_CONNECTION_ENGINE;

/**
 * Creates the Rest Engine (currently only unirest) and provides the methods to
 * invoke activity and lookup calls
 */
public class BreinEngine {

    /**
     * creation of rest com.brein.engine.
     */
    private IRestEngine restEngine = null;

    /**
     * Creates the com.brein.engine
     */
    public BreinEngine() {
        restEngine = new HttpUrlRestEngine();
    }

    /**
     * sends an activity to the breinify server
     *
     * @param activity data
     */
    public void sendActivity(final BreinActivity activity) {
        if (activity != null) {
            restEngine.doRequest(activity);
        }
    }

    /**
     * performs a lookup. This will be delegated to the
     * configured restEngine.
     *
     * @param breinLookup contains the appropriate data for the lookup
     *                    request
     * @return if succeeded a BreinResponse object or  null
     */
    public BreinResult performLookUp(final BreinLookup breinLookup) {
        if (breinLookup != null) {
            return restEngine.doLookup(breinLookup);
        }
        return null;
    }

    /**
     * returns the brein com.brein.engine
     *
     * @return com.brein.engine itself
     */
    public IRestEngine getRestEngine() {
        return restEngine;
    }

    /**
     * configuration of com.brein.engine
     *
     * @param breinConfig configuration object
     */
    public void configure(final BreinConfig breinConfig) {
        restEngine.configure(breinConfig);
    }

    BreinEngineType getRestEngineType(final BreinEngineType engine) {
        return HTTP_URL_CONNECTION_ENGINE;
    }
    
    public void invoke(final BreinConfig config, final BreinBase data, final ICallback<BreinResult> callback) {
        getEngine(config).invokeRequest(config, data, callback);
    }

    protected IRestEngine getEngine(final BreinConfig config) {
        return restEngine;
    }

    public void terminate() {
    }

}

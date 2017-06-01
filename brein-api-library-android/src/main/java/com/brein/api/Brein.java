package com.brein.api;

import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.domain.results.BreinTemporalDataResult;
import com.brein.engine.BreinEngine;


public class Brein {
    private BreinConfig config;
    private BreinEngine engine;

    /**
     * Sets the configuration
     *
     * @param breinConfig config object
     */
    public Brein setConfig(final BreinConfig breinConfig) {
        if (this.engine != null) {
            shutdown();
        }

        this.config = breinConfig;

        return this;
    }

    /**
     * Sends an activity to the engine utilizing the API. The call is done asynchronously as a POST request. It is
     * important that a valid API-key is configured prior to using this function.
     * <p>
     * This request is asynchronous.
     */
    public void activity(final BreinActivity data, final ICallback<BreinResult> callback) {
        getEngine().invoke(this.config, data, callback);
    }

    /**
     * Retrieves a lookup result from the engine. The function needs a valid API-key to be configured to succeed.
     * <p>
     * This request is synchronous.
     *
     * @param data a plain object specifying the lookup information.
     *
     * @return response from request wrapped in an object called BreinResponse
     */
    public void lookup(final BreinLookup data, final ICallback<BreinResult> callback) {
        getEngine().invoke(this.config, data, callback);
    }

    /**
     * Sends a temporalData to the engine utilizing the API. The call is done synchronously as a POST request. It is
     * important that a valid API-key is configured prior to using this function.
     * <p>
     * This request is synchronous.
     *
     * @return result from the Breinify engine
     */
    public void temporalData(final BreinTemporalData data, final ICallback<BreinResult> callback) {
        getEngine().invoke(this.config, data, callback);
        // return new BreinTemporalDataResult(result.getMap());
    }


    /**
     * Sends a recommendation request to the engine utilizing the API. The call is done synchronously as a POST request.
     * It is important that a valid API-key is configured prior to using this function.
     * <p>
     * This request is synchronous.
     *
     * @param data contains the brein recommendation object
     *
     */
    public void recommendation(final BreinRecommendation data, final ICallback<BreinResult> callback) {
        getEngine().invoke(this.config, data, callback);
    }

    /**
     * Shutdown Breinify services
     */
    public void shutdown() {
        if (this.engine != null) {
            this.engine.terminate();
            this.engine = null;
        }
    }

    public BreinEngine getEngine() {
        if (this.engine == null) {
            this.engine = new BreinEngine();
        }

        return engine;
    }

    public BreinConfig getConfig() {
        return config;
    }
}

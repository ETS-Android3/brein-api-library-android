package com.brein.engine;

import com.brein.api.BreinActivity;
import com.brein.api.BreinBase;
import com.brein.api.BreinException;
import com.brein.api.BreinLookup;
import com.brein.api.ICallback;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;


/**
 * Interface for all possible rest  engines
 */
public interface IRestEngine {

    /**
     * configures the rest engine
     *
     * @param breinConfig configuration object
     */
    void configure(final BreinConfig breinConfig);

    /**
     * invokes the post request
     *
     * @param breinActivity data
     */
    void doRequest(final BreinActivity breinActivity) throws BreinException;

    /**
     * performs a lookup and provides details
     *
     * @param breinLookup contains request data
     * @return response from Breinify
     */
    BreinResult doLookup(final BreinLookup breinLookup) throws BreinException;

    /**
     * terminates the rest engine
     */
    void terminate();

    /**
     *
     * @param engine
     * @return
     */
    IRestEngine getRestEngine(final BreinEngineType engine);

    /**
     *
     * @param engine
     * @return
     */
    BreinEngineType getRestEngineType(final BreinEngineType engine);

    /**
     *
     * @param config
     * @param data
     * @param callback
     */
    void invokeRequest(final BreinConfig config,
                           final BreinBase data,
                           final ICallback<BreinResult> callback);

}

package com.brein.api;

import com.brein.domain.BreinConfig;

import java.util.Map;

/**
 * Base class for the secret strategy
 */
public interface ISecretStrategy {

    /**
     * Creates the appropriate signature that is part of the request to
     * the Breinify server.
     *
     * @return creates signature
     */
    String createSignature(final BreinConfig config, final Map<String, Object> requestData);
}

package com.brein.api;

import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.util.BreinUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Provides the lookup functionality
 */

public class BreinLookup extends BreinBase<BreinActivity> implements ISecretStrategy, IAsyncExecutable {

    /**
     * used for lookup request
     */
    private BreinDimension breinDimension;

    /**
     * retrieves the Brein dimension object
     *
     * @return object
     */
    public BreinDimension getBreinDimension() {
        return breinDimension;
    }

    /**
     * sets the breindimension object - will be used for lookup
     *
     * @param breinDimension object to set
     */
    public BreinLookup setBreinDimension(BreinDimension breinDimension) {
        this.breinDimension = breinDimension;
        return this;
    }

    /**
     * initializes the values of this instance
     */
    public void init() {
        breinDimension = null;
    }

    /**
     * resets all values of this class and base class to initial values.
     * This will lead to empty strings or null objects
     */
    public void resetAllValues() {
        // reset init values
        init();

        // reset base values (User & Config)
        // TODO check super.init call
        // super.init();
    }

    public void prepareRequestData(BreinConfig config, Map<String, Object> requestData) {

    }

    /**
     * prepares a JSON request for a lookup
     *
     * @return well formed json request
     */
    @Override
    public String prepareRequestData(final BreinConfig config) {

        // call base class
        super.prepareRequestData(config);

        final JsonObject requestData = new JsonObject();
        final BreinUser breinUser = getUser();
        if (breinUser != null) {
            JsonObject userData = new JsonObject();
            userData.addProperty("email", breinUser.getEmail());
            requestData.add("user", userData);
        }

        /*
         * Dimensions
         */
        if (BreinUtil.containsValue(getBreinDimension())) {
            final JsonObject lookupData = new JsonObject();
            final JsonArray dimensions = new JsonArray();
            for (String field : getBreinDimension().getDimensionFields()) {
                dimensions.add(field);
            }
            lookupData.add("dimensions", dimensions);
            requestData.add("lookup", lookupData);
        }

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();

        return gson.toJson(requestData);
    }

    /**
     * retrieves the configured lookup endpoint (e.g. \lookup)
     *
     * @return endpoint
     */
    @Override
    public String getEndPoint(final BreinConfig config) {
        return config.getLookupEndpoint();
    }

    /**
     * Creates the signature for lookup
     *
     * @return signature
     */
    @Override
    public String createSignature(final BreinConfig config, final Map<String, Object> requestData) {

        final String[] dimensions = getBreinDimension().getDimensionFields();

        // we need the first one
        final String message = String.format("%s%d%d",
                dimensions == null ? 0 : dimensions[0],
                getUnixTimestamp(),
                dimensions == null ? 0 : dimensions.length);

        return BreinUtil.generateSignature(message, config.getSecret());
    }

    @Override
    public void execute(ICallback callback) {
        Breinify.lookUp(this, callback);
    }

}

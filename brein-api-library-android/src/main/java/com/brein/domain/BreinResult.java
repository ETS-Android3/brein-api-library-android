package com.brein.domain;

import com.brein.util.BreinMapUtil;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Contains the result of an Brein Request when invoking a
 * request
 */
public class BreinResult {

    /**
     * contains the collected data as map
     */
    private final Map<String, Object> map;

     /**
     * creates a brein result object
     * @param jsonResponse as json string
     */
    @SuppressWarnings("unchecked")
    public BreinResult(final String jsonResponse) {
        map = new Gson().fromJson(jsonResponse, Map.class);
    }

    public BreinResult(final Map<String, Object> json) {
        this.map = json;
    }

    /**
     * retrieves the object according to the requested key
     * @param key to look for
     * @param <T> Object
     * @return Object retrieved
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        return (T) map.get(key);
    }

    /**
     * checks if key exists in map
     * @param key to check
     * @return true or false
     */
    public boolean has(final String key) {
        return get(key) != null;
    }

    /**
     * provides the map containing the results
     *
     * @return map of String, Object
     */
    public Map<String, Object> getMap() {
        return map;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final String key) {
        return map == null ? null : (T) map.get(key);
    }

    public boolean hasValue(final String key) {
        return map != null && map.containsKey(key);
    }

    public <T> T getNestedValue(final String... keys) {
        return BreinMapUtil.getNestedValue(map, keys);
    }

    public boolean hasNestedValue(final String... keys) {
        return BreinMapUtil.hasNestedValue(map, keys);
    }

    public String getMessage() {
        if (has("message")) {
            return get("message");
        }

        return null;
    }

}

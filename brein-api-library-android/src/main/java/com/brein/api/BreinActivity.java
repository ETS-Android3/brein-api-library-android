package com.brein.api;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;

import com.brein.domain.BreinUser;
import com.brein.util.BreinUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * Sends an activity to the com.brein.engine utilizing the API. The call is done asynchronously as a POST request. It is important
 * that a valid API-key is configured prior to using this function.
 */
public class BreinActivity extends BreinBase implements ISecretStrategy {

    /**
     * ActivityType of the activity
     */
    private String breinActivityType;

    /**
     * Category of the activity
     */
    private String breinCategoryType;

    /**
     * Description of the activity
     */
    private String description;

    /**
     * contains the tags
     */
    private Map<String, Object> tagsMap;

    /**
     * returns activity type
     *
     * @return activity type
     */
    public String getBreinActivityType() {
        return breinActivityType;
    }

    /**
     * Sets activity type
     *
     * @param breinActivityType to set
     */
    public BreinActivity setBreinActivityType(final String breinActivityType) {
        this.breinActivityType = breinActivityType;
        return this;
    }

    /**
     * retrieves brein category
     *
     * @return category object
     */
    public String getBreinCategoryType() {
        return breinCategoryType;
    }

    /**
     * sets brein category
     *
     * @param breinCategoryType object
     */
    public BreinActivity setBreinCategoryType(final String breinCategoryType) {
        this.breinCategoryType = breinCategoryType;
        return this;
    }

    /**
     * retrieves the description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description
     *
     * @param description string to set as description
     */
    public BreinActivity setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * retrieves the configured activity endpoint (e.g. \activitiy)
     *
     * @return endpoint
     */
    @Override
    public String getEndPoint() {
        return getConfig().getActivityEndpoint();
    }

    /**
     * Sends an activity to the Breinify server.
     *
     * @param breinUser         the user-information
     * @param breinActivityType the type of activity
     * @param breinCategoryType the category (can be null or undefined)
     * @param description       the description for the activity
     * @param sign              true if a signature should be added (needs the secret to be configured - not recommended
     *                          in open systems), otherwise false (can be null or undefined)
     */
    public void activity(final BreinUser breinUser,
                         final String breinActivityType,
                         final String breinCategoryType,
                         final String description,
                         final boolean sign) {

        /*
         * set the values for further usage
         */
        setBreinUser(breinUser);
        setBreinActivityType(breinActivityType);
        setBreinCategoryType(breinCategoryType);
        setDescription(description);
        setSign(sign);

        /*
         * invoke the request, "this" has all necessary information
         */
        if (null == getBreinEngine()) {
            throw new BreinException(BreinException.ENGINE_NOT_INITIALIZED);
        }
        getBreinEngine().sendActivity(this);
    }

    /**
     * retrieves the tagMap
     *
     * @return value
     */
    public Map<String, Object> getTagsMap() {
        return tagsMap;
    }

    /**
     * sets the tagsMap
     *
     * @param tagsMap created map (e.g. HashMap)
     * @return self
     */
    public BreinActivity setTagsMap(final Map<String, Object> tagsMap) {
        this.tagsMap = tagsMap;
        return this;
    }

    /**
     * initializes the values of this instance
     */
    public void init() {
        breinActivityType = "";
        breinCategoryType = "";
        description = "";
        tagsMap = null;
    }

    /**
     * creates the json request based on the necessary data
     *
     * @return json string
     */
    @Override
    public String prepareJsonRequest() {

        // call base class
        super.prepareJsonRequest();

        final JsonObject requestData = new JsonObject();

        // user data
        final BreinUser breinUser = getBreinUser();
        if (breinUser != null) {

            final JsonObject userData = new JsonObject();
            if (BreinUtil.containsValue(breinUser.getEmail())) {
                userData.addProperty("email", breinUser.getEmail());
            }

            if (BreinUtil.containsValue(breinUser.getFirstName())) {
                userData.addProperty("firstName", breinUser.getFirstName());
            }

            if (BreinUtil.containsValue(breinUser.getLastName())) {
                userData.addProperty("lastName", breinUser.getLastName());
            }

            if (BreinUtil.containsValue(breinUser.getDateOfBirth())) {
                userData.addProperty("dateOfBirth", breinUser.getDateOfBirth());
            }

            if (BreinUtil.containsValue(breinUser.getDeviceId())) {
                userData.addProperty("deviceId", breinUser.getDeviceId());
            }

            if (BreinUtil.containsValue(breinUser.getImei())) {
                userData.addProperty("imei", breinUser.getImei());
            }

            if (BreinUtil.containsValue(breinUser.getSessionId())) {
                userData.addProperty("sessionId", breinUser.getSessionId());
            }

            // additional part
            final JsonObject additional = new JsonObject();
            if (BreinUtil.containsValue(breinUser.getUserAgent())) {
                additional.addProperty("userAgent", breinUser.getUserAgent());
            }

            if (BreinUtil.containsValue(breinUser.getReferrer())) {
                additional.addProperty("referrer", breinUser.getReferrer());
            }

            if (BreinUtil.containsValue(breinUser.getUrl())) {
                additional.addProperty("url", breinUser.getUrl());
            }

            if (BreinUtil.containsValue(breinUser.getIpAddress())) {
                additional.addProperty("ipAddress", breinUser.getIpAddress());
            }

            // add coordinates (if available)
            handleGpsCoordinates(additional);

            handleNetworkInfo(additional);

            if (!additional.isJsonNull()) {
                userData.add("additional", additional);
            }

            requestData.add("user", userData);
        }

        // activity data
        final JsonObject activityData = new JsonObject();
        if (BreinUtil.containsValue(getBreinActivityType())) {
            activityData.addProperty("type", getBreinActivityType());
        }
        if (BreinUtil.containsValue(getDescription())) {
            activityData.addProperty("description", getDescription());
        }
        if (BreinUtil.containsValue(getBreinCategoryType())) {
            activityData.addProperty("category", getBreinCategoryType());
        }

        // tags
        final Map<String, Object> tagsMap = getTagsMap();
        if (tagsMap != null && tagsMap.size() > 0) {
            final JsonObject tagsData = new JsonObject();

            // Android does not support Lambdas (yet)
            //loop a Map
            for (Map.Entry<String, Object> entry : tagsMap.entrySet()) {
                if (entry.getValue().getClass() == String.class) {
                    tagsData.addProperty(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue().getClass() == Double.class ||
                        entry.getValue().getClass() == Integer.class) {
                    tagsData.addProperty(entry.getKey(), (Number) entry.getValue());
                } else if (entry.getValue().getClass() == Boolean.class) {
                    tagsData.addProperty(entry.getKey(), (Boolean) entry.getValue());
                }
            }

            activityData.add("tags", tagsData);
        }

        requestData.add("activity", activityData);

        /*
         * further data...
         */
        if (BreinUtil.containsValue(getConfig())) {
            if (BreinUtil.containsValue(getConfig().getApiKey())) {
                requestData.addProperty("apiKey", getConfig().getApiKey());
            }
        }

        requestData.addProperty("unixTimestamp", getUnixTimestamp());

        // if sign is active
        if (isSign()) {
            requestData.addProperty("signatureType", createSignature());
        }

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();

        return gson.toJson(requestData);
    }

    /**
     * Provides network information within the user additional request
     *
     * @param additional additional json object
     */
    public void handleNetworkInfo(final JsonObject additional) {

        // firstly get the context
        final Context context = getConfig().getApplicationContext();
        if (context == null) {
            return;
        }

        final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                final String ssid = wifiInfo.getSSID();
                final String bssid = wifiInfo.getBSSID();
                // final boolean hiddenSsid = wifiInfo.getHiddenSSID();

                int ip = wifiInfo.getIpAddress();
                final String ipAddress = Formatter.formatIpAddress(ip);
                final int linkSpeed = wifiInfo.getLinkSpeed();
                final String macAddress = wifiInfo.getMacAddress();
                final int rssi = wifiInfo.getRssi();
                final int networkId = wifiInfo.getNetworkId();
                final String state = wifiInfo.getSupplicantState().toString();

                final JsonObject networkData = new JsonObject();
                networkData.addProperty("ssid", ssid);
                networkData.addProperty("bssid", bssid);
                networkData.addProperty("ipAddress", ipAddress);
                networkData.addProperty("linkSpeed", linkSpeed);
                networkData.addProperty("macAddress", macAddress);
                networkData.addProperty("rssi", rssi);
                networkData.addProperty("networkId", networkId);
                networkData.addProperty("state", state);

                additional.add("network", networkData);
            }
        }
    }

    /**
     * adds the GPS coordinates to the json request
     *
     * @param additional
     */
    public void handleGpsCoordinates(final JsonObject additional) {

        // firstly get the context
        final Context context = getConfig().getApplicationContext();
        if (context == null) {
            return;
        }

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final List<String> providers = locationManager.getProviders(true);

        // Loop over the array backwards, and if you get an accurate location, then break out the loop
        Location location = null;

        for (int index = providers.size() - 1; index >= 0; index--) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // we do not have the permissions
                return;
            }
            location = locationManager.getLastKnownLocation(providers.get(index));
            if (location != null) {
                break;
            }
        }

        if (location != null) {
            final JsonObject locationData = new JsonObject();
            locationData.addProperty("accuracy", location.getAccuracy());
            locationData.addProperty("speed", location.getSpeed());
            locationData.addProperty("latitude", location.getLatitude());
            locationData.addProperty("longitude", location.getLongitude());

            additional.add("location", locationData);
        }
    }

    /**
     * Generates the signature for the request
     *
     * @return full signature
     */
    @Override
    public String createSignature() {

        final String message = String.format("%s%d%d",
                getBreinActivityType() == null ? "" : getBreinActivityType(),
                getUnixTimestamp(), 1);
        // activities.size());

        return BreinUtil.generateSignature(message, getConfig().getSecret());
    }
}


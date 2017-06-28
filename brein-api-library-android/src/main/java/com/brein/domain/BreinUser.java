package com.brein.domain;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;

import com.brein.api.BreinBase;
import com.brein.api.BreinException;
import com.brein.util.BreinMapUtil;
import com.brein.util.BreinUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;
import static com.brein.api.Breinify.getConfig;

/**
 * A plain object specifying the user information the activity belongs to
 */
public class BreinUser {

    public static final String USER_FIELD = "user";
    public static final String ADDITIONAL_FIELD = "additional";

    /**
     * contains further fields in the user additional section
     */
    private Map<String, Object> additionalMap = new HashMap<>();
    /**
     * contains further fields in the user section
     */
    private Map<String, Object> userMap;

    /**
     * create a brein user with mandatory field email.
     *
     * @param email of the user
     */
    public BreinUser(final String email) {
        UserField.EMAIL.set(this, email);
    }

    /**
     * create a brein user
     */
    public BreinUser() {
    }

    /**
     * get the email of the user
     *
     * @return email
     */
    public String getEmail() {
        return getUserField(UserField.EMAIL);
    }

    /**
     * sets the email of the user
     *
     * @param email to set (will not be checked)
     * @return this
     */
    public BreinUser setEmail(final String email) {
        UserField.EMAIL.set(this, email);
        return this;
    }

    /**
     * Retrieves the first name of the user
     *
     * @return first name
     */
    public String getFirstName() {
        return getUserField(UserField.FIRST_NAME);
    }

    /**
     * set the first name of the user
     *
     * @param firstName name to set
     * @return this
     */
    public BreinUser setFirstName(final String firstName) {
        UserField.FIRST_NAME.set(this, firstName);
        return this;
    }

    /**
     * Retrieves the last name of the user
     *
     * @return last name
     */
    public String getLastName() {
        return getUserField(UserField.LAST_NAME);
    }

    /**
     * set the last name of the user
     *
     * @param lastName last name
     * @return thi
     */
    public BreinUser setLastName(final String lastName) {
        UserField.LAST_NAME.set(this, lastName);
        return this;
    }

    /**
     * returns the sessionId (if set)
     *
     * @return sessionId
     */
    public String getSessionId() {
        return getUserField(UserField.SESSION_ID);
    }

    /**
     * sets the sessionId
     *
     * @param sessionId value of the sessionId
     * @return self
     */
    public BreinUser setSessionId(final String sessionId) {
        UserField.SESSION_ID.set(this, sessionId);
        return this;
    }

    /**
     * Returns the date of birth
     *
     * @return date of birth
     */
    public String getDateOfBirth() {
        return getUserField(UserField.DATE_OF_BIRTH);
    }

    /**
     * Sets the value of dateOfBirth as String. This is only used internally.
     *
     * @param dateOfBirthString contains the date of birth string
     * @return self
     */
    public BreinUser setDateOfBirth(final String dateOfBirthString) {
        UserField.DATE_OF_BIRTH.set(this, dateOfBirthString);
        return this;
    }

    /**
     * Set's the date of birth
     * There is no check if the month - day combination is valid, only
     * the range for day, month and year will be checked
     *
     * @param month (1..12)
     * @param day   (1..31)
     * @param year  (1900..2100)
     * @return self
     */
    public BreinUser setDateOfBirth(final int month, final int day, final int year) {
        if ((month >= 1 && month <= 12) &&
                (day >= 1 && day <= 31) &&
                (year >= 1900 && year <= 2100)) {
            setDateOfBirth(Integer.toString(month)
                    + "/"
                    + Integer.toString(day)
                    + "/"
                    + Integer.toString(year));
        } else {
            setDateOfBirth("");
        }

        return this;
    }

    /**
     * resets the dateOfBirth to an empty value
     */
    public void resetDateOfBirth() {
        UserField.DATE_OF_BIRTH.set(this, "");
    }

    /**
     * Retrieves imei (International Mobile Equipment Identity)
     *
     * @return serial number as string
     */
    public String getImei() {
        return getUserField(UserField.IMEI);
    }

    /**
     * Sets the imei number
     *
     * @param imei number
     * @return this -> allows chaining
     */
    public BreinUser setImei(final String imei) {
        UserField.IMEI.set(this, imei);
        return this;
    }

    /**
     * retrieves the deviceid
     *
     * @return device id
     */
    public String getDeviceId() {
        return getUserField(UserField.DEVICE_ID);
    }

    /**
     * sets the device id
     *
     * @param deviceId the id of the device
     * @return this -> allows chaining
     */
    public BreinUser setDeviceId(final String deviceId) {
        UserField.DEVICE_ID.set(this, deviceId);
        return this;
    }

    /**
     * retrieves the userId
     *
     * @return userId String
     */
    public String getUserId() {
        return getUserField(UserField.USER_ID);
    }

    /**
     * sets the userId
     *
     * @param userId the id of the user
     * @return this -> allows chaining
     */
    public BreinUser setUserId(final String userId) {
        UserField.USER_ID.set(this, userId);
        return this;
    }

    /**
     * sets the phone
     *
     * @param phone the phone of the user
     * @return this
     */
    public BreinUser setPhone(final String phone) {
        UserField.PHONE.set(this, phone);
        return this;
    }

    /**
     * retrieves the phone
     *
     * @return phone String
     */
    public String getPhone() {
        return getUserField(UserField.PHONE);
    }


    /**
     * retrieves the additional userAgent value
     *
     * @return value
     */
    public String getUserAgent() {
        return getUserAdditionalField(UserAdditionalField.USER_AGENT);
    }

    /**
     * sets the additional user agent value
     *
     * @param userAgent value
     */
    public BreinUser setUserAgent(final String userAgent) {
        UserAdditionalField.USER_AGENT.set(this, userAgent);
        return this;
    }

    /**
     * @return
     */
    public String createUserAgent() {
        // TODO: 27/06/2017 logic missing
        final String userAgent = System.getProperty("http.agent");
        return userAgent;
    }

    /**
     * retrieves the ipAddress (additional part)
     *
     * @return ipAddress
     */
    public String getIpAddress() {
        return getUserAdditionalField(UserAdditionalField.IPADDRESS);
    }

    /**
     * sets the ipAddress
     *
     * @param ipAddress value
     */
    public BreinUser setIpAddress(final String ipAddress) {
        UserAdditionalField.IPADDRESS.set(this, ipAddress);
        return this;
    }

    /**
     * retrieves the additional referrer value
     *
     * @return value
     */
    public String getReferrer() {
        return getUserAdditionalField(UserAdditionalField.REFERRER);
    }

    /**
     * sets the additional referrer value
     *
     * @param referrer value
     */
    public BreinUser setReferrer(final String referrer) {
        UserAdditionalField.REFERRER.set(this, referrer);
        return this;
    }

    /**
     * retrieves the timezone
     *
     * @return value
     */
    public String getTimezone() {
        return getUserAdditionalField(UserAdditionalField.TIME_ZONE);
    }

    /**
     * sets the timezone
     *
     * @param timezone value
     * @return self
     */
    public BreinUser setTimezone(final String timezone) {
        UserAdditionalField.TIME_ZONE.set(this, timezone);
        return this;
    }

    /**
     * retrieves the additional url
     *
     * @return value
     */
    public String getUrl() {
        return getUserAdditionalField(UserAdditionalField.URL);
    }

    /**
     * sets the additional url
     *
     * @param url value
     * @return self
     */
    public BreinUser setUrl(final String url) {
        UserAdditionalField.URL.set(this, url);
        return this;
    }

    /**
     * retrieves the localDateTime
     *
     * @return value
     */
    public String getLocalDateTime() {
        return getUserAdditionalField(UserAdditionalField.LOCAL_DATE_TIME);
    }

    /**
     * sets the localDateTime value
     *
     * @param localDateTime value
     * @return self
     */
    public BreinUser setLocalDateTime(final String localDateTime) {
        UserAdditionalField.LOCAL_DATE_TIME.set(this, localDateTime);
        return this;
    }

    /**
     * retrieves the pushDeviceToken
     *
     * @return value
     */
    public String getPushDeviceRegistration() {
        return getUserAdditionalField(UserAdditionalField.PUSH_DEVICE_REGISTRATION);
    }

    /**
     * sets the pushDeviceToken
     *
     * @param deviceToken value
     * @return self
     */
    public BreinUser setPushDeviceRegistration(final String deviceToken) {
        UserAdditionalField.PUSH_DEVICE_REGISTRATION.set(this, deviceToken);
        return this;
    }

    /**
     * detects the GPS coordinates and adds this to the user.additional.location section
     */
    public void detectGpsCoordinates() {

        // firstly get the context
        final Application applicationContext = getConfig().getApplication();
        if (applicationContext == null) {
            return;
        }

        final LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
        final List<String> providers = locationManager.getAllProviders(); // getProviders(true);

        // Loop over the array backwards, and if you get an accurate location, then break out the loop
        Location location = null;

        for (int index = providers.size() - 1; index >= 0; index--) {

            final int accessFineLocationPermission = ActivityCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            final int accessCoarseLocationPermission = ActivityCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (accessCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            /*
            if (ActivityCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // we do not have the permissions
                return;
            }
            */
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

            this.additionalMap.put("location", locationData);
        }
    }

    /**
     * Provides network information within the user additional request
     */
    public void detectNetwork() {

        // firstly get the context
        final Application applicationContext = getConfig().getApplication();
        if (applicationContext == null) {
            return;
        }

        // only possible if permission has been granted
        if (ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

            final WifiManager wifiManager = (WifiManager) applicationContext
                    .getApplicationContext()
                    .getSystemService(WIFI_SERVICE);

            if (wifiManager != null) {
                final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {

                    // TODO: 27/06/2017 check all information!!!

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

                    if (this.additionalMap != null) {
                        this.additionalMap.put("network", networkData);
                    }
                }
            }
        }
    }

    /**
     * provides a nicer output of the user details
     *
     * @return output
     */
    @Override
    public String toString() {
        final BreinConfig config = new BreinConfig(null);
        final Map<String, Object> requestData = new HashMap<>();

        prepareRequestData(config, requestData);
        return BreinBase.GSON.toJson(requestData);
    }

    /**
     * Sets the users value and overrides any current value. Cannot used to override the {@code additional} field.
     *
     * @param key   the name of the value to be set
     * @param value the value to be set
     * @return self
     */
    public BreinUser set(final String key, final Object value) {
        if (ADDITIONAL_FIELD.equalsIgnoreCase(key)) {
            throw new BreinException("The field '" + ADDITIONAL_FIELD + "' cannot be set, " +
                    "use the setAdditional method to do so.");
        } else if (this.userMap == null) {
            this.userMap = new HashMap<>();
        }

        this.userMap.put(key, value);
        return this;
    }

    /**
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(final String key) {
        return get(key, false);
    }

    /**
     * @param key
     * @param additional
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final boolean additional) {
        if (additional) {
            return this.additionalMap == null ? null : (T) this.additionalMap.get(key);
        } else {
            return this.userMap == null ? null : (T) this.userMap.get(key);
        }
    }

    /**
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getAdditional(final String key) {
        return get(key, true);
    }

    /**
     * Sets an additional value.
     *
     * @param key   the name of the additional value to be set
     * @param value the value to be set
     * @return self
     */
    public BreinUser setAdditional(final String key, final Object value) {
        if (this.additionalMap == null) {
            this.additionalMap = new HashMap<>();
        }

        this.additionalMap.put(key, value);
        return this;
    }

    /**
     * prepares the request data
     *
     * @param config      contains the configuration (if necessary)
     * @param requestData request destination
     */
    public void prepareRequestData(final BreinConfig config, final Map<String, Object> requestData) {
        final Map<String, Object> userRequestData = new HashMap<>();
        requestData.put(USER_FIELD, userRequestData);

        // add the user-data, if there is any
        if (this.userMap != null) {
            // loop a Map
            for (Map.Entry<String, Object> entry : this.userMap.entrySet()) {
                if (BreinUtil.containsValue(entry.getValue())) {
                    userRequestData.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // tries to detect gps and network data this will be added to property additionalMap
        detectGpsCoordinates();
        detectNetwork();

        // check or create userAgent
        handleUserAgent();

        // add the additional-data, if there is any
        if (this.additionalMap != null) {
            userRequestData.put(ADDITIONAL_FIELD, BreinMapUtil.copyMap(this.additionalMap));
        }
    }

    /**
     * Checks if a userAgent has been set if not it will be generated and set.
     */
    public void handleUserAgent() {
        final String userAgent = this.getUserAgent();
        if (userAgent != null && userAgent.length() != 0) {
            setUserAgent(createUserAgent());
        }
    }

    /**
     * @param field
     * @param <T>
     * @return
     */
    protected <T> T getUserField(final UserField field) {
        return get(field.getName());
    }

    /**
     * This list may not be complete it just contains some values. For a complete list it is recommended to look at the
     * API documentation.
     */
    public enum UserField {
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        DATE_OF_BIRTH("dateOfBirth"),
        EMAIL("email"),
        SESSION_ID("sessionId"),
        IMEI("imei"),
        DEVICE_ID("deviceId"),
        PHONE("phone"),
        USER_ID("userId");

        final String name;

        UserField(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void set(final BreinUser user, final Object value) {
            user.set(getName(), value);
        }
    }

    /**
     * @param field
     * @param <T>
     * @return
     */
    protected <T> T getUserAdditionalField(final UserAdditionalField field) {
        return get(field.getName());
    }


    /**
     * This is part of the User.Additional Section
     */
    public enum UserAdditionalField {

        IPADDRESS("ipAddress"),
        REFERRER("referrer"),
        URL("url"),
        USER_AGENT("userAgent"),
        TIME_ZONE("timezone"),
        LOCAL_DATE_TIME("localDateTime"),
        PUSH_DEVICE_REGISTRATION("androidPushDeviceRegistration");

        final String name;

        UserAdditionalField(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void set(final BreinUser user, final Object value) {
            user.setAdditional(getName(), value);
        }
    }
}


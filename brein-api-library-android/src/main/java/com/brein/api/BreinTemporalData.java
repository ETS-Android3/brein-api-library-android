package com.brein.api;

import com.brein.domain.BreinConfig;
import com.brein.domain.BreinUser;
import com.brein.util.BreinMapUtil;
import com.brein.util.BreinUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * Sends an activity to the Brein engine utilizing the API.
 *
 * The call is done asynchronously as a POST request. It is important
 * that a valid API-key & secret is configured prior before using this class.
 */
public class BreinTemporalData extends BreinBase<BreinTemporalData> implements ISecretStrategy, IAsyncExecutable {

    // The following fields are used within the additional
    private static final String LOCATION_FIELD = "location";
    private static final String LOCAL_DATE_TIME_FIELD = "localDateTime";
    private static final String TIMEZONE_FIELD = "timezone";
    private static final String IP_ADDRESS_FIELD = "ipAddress";

    // The following fields are used within the location
    private static final String LONGITUDE_FIELD = "longitude";
    private static final String LATITUDE_FIELD = "latitude";
    private static final String SHAPE_TYPES_FIELD = "shapeTypes";
    private static final String TEXT_FIELD = "text";

    private static final String CITY_TEXT_FIELD = "text";
    private static final String STATE_TEXT_FIELD = "text";
    private static final String COUNTRY_TEXT_FIELD = "text";

    /**
     * retrieves the configured temporalData endpoint (e.g. \temporalData)
     *
     * @return  String endpoint
     */
    @Override
    public String getEndPoint(final BreinConfig config) {
        return config.getTemporalDataEndpoint();
    }

    @Override
    public void prepareRequestData(final BreinConfig config, final Map<String, Object> requestData) {
        // nothing additionally to be added
    }

    /**
     * Sets the timezone within the request, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  'timezone': timezone
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param timezone TimeZone the value to be set
     *
     * @return {@code this}
     */
    public BreinTemporalData setTimezone(final TimeZone timezone) {
        return setTimezone(timezone == null ? null : timezone.getID());
    }

    /**
     * Sets the timezone within the request, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  'timezone': timezone
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param timezone String the value to be set
     *
     * @return {@code this}
     */
    public BreinTemporalData setTimezone(final String timezone) {
        setAdditional(TIMEZONE_FIELD, timezone);
        return this;
    }

    /**
     * Sets the ipAddress used to look-up the temporal information, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  'ipAddress': ipAddress
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param ipAddress String the value to be set
     *
     * @return {@code this}
     */
    public BreinTemporalData setLookUpIpAddress(final String ipAddress) {
        setAdditional(IP_ADDRESS_FIELD, ipAddress);
        return this;
    }

    /**
     * Sets the longitude used to look-up the temporal information, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'longitude': longitude
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param longitude double the longitude to be used
     *
     * @return {@code this}
     */
    public BreinTemporalData setLongitude(final double longitude) {
        setLocation(LONGITUDE_FIELD, longitude);
        return this;
    }

    /**
     * Sets the latitude used to look-up the temporal information, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'latitude': latitude
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param latitude double the latitude to be used
     *
     * @return {@code this}
     */
    public BreinTemporalData setLatitude(final double latitude) {
        setLocation(LATITUDE_FIELD, latitude);
        return this;
    }

    /**
     * Sets the shape-types to be returned with the response of the request, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'shapeTypes': [...]
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param shapeTypes String the shapeTypes to be set, if empty the shapeTypes will be removed
     *
     * @return {@code this}
     */
    public BreinTemporalData setShapeTypes(final String... shapeTypes) {
        if (shapeTypes == null || shapeTypes.length == 0) {
            setLocation(SHAPE_TYPES_FIELD, null);
        } else {
            setLocation(SHAPE_TYPES_FIELD, new ArrayList<>(Arrays.asList(shapeTypes)));
        }
        return this;
    }

    /**
     * Sets the location data using free text, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'text': freeText
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param freeText String the text describing the location
     *
     * @return {@code this}
     */
    public BreinTemporalData setLocation(final String freeText) {
        setLocation(TEXT_FIELD, freeText);
        return this;
    }

    /**
     * Sets the localDateTime based on the system's time, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  'localDateTime': now
     *              }
     *         }
     *     }
     * </pre>
     *
     * @return {@code this}
     *
     */
    public BreinTemporalData setLocalDateTime() {
        final TimeZone defTimeZone = TimeZone.getDefault();
        final Calendar c = Calendar.getInstance();
        final Date date = c.getTime();
        final SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ZZZZ (zz)", Locale.US);
        df.setTimeZone(defTimeZone);
        final String strLocalDateTimeValue = df.format(date);

        return setLocalDateTime(strLocalDateTimeValue);
    }

    /**
     * Sets the localDateTime, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  'localDateTime': zonedDateTime
     *              }
     *         }
     *     }
     * </pre>
     *
     * @return {@code this}
     */
    public BreinTemporalData setLocalDateTime(final String localDateTime) {
        setAdditional(LOCAL_DATE_TIME_FIELD, localDateTime);
        return this;
    }

    /**
     * Sets the location data using structured data (city, state, country), i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'city': city,
     *                      'state': state,
     *                      'country': country
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param city    String the city to look up
     * @param state   String the state to look up (can be {@code null})
     * @param country String the country to look up (can be {@code null})
     *
     * @return {@code this}
     */
    public BreinTemporalData setLocation(final String city, final String state, final String country) {
        setLocation(CITY_TEXT_FIELD, city);
        setLocation(STATE_TEXT_FIELD, state);
        setLocation(COUNTRY_TEXT_FIELD, country);
        return this;
    }

    /**
     * Adds the specified {@code shapeTypes} to the currently defined shape-types to be returned with the response of
     * the request, i.e.:
     * <p>
     * <pre>
     *     {
     *         user: {
     *              additional: {
     *                  location: {
     *                      'shapeTypes': [...]
     *                  }
     *              }
     *         }
     *     }
     * </pre>
     *
     * @param shapeTypes String the shapeTypes to be added
     *
     * @return {@code this}
     */
    public BreinTemporalData addShapeTypes(final String... shapeTypes) {
        if (shapeTypes == null || shapeTypes.length == 0) {
            return this;
        }

        List<String> list = getLocation(SHAPE_TYPES_FIELD);
        if (list == null) {
            list = new ArrayList<>();
            setLocation(SHAPE_TYPES_FIELD, list);
        }
        list.addAll(Arrays.asList(shapeTypes));

        return this;
    }

    /**
     * Gets the current value specified within the location of the request.
     *
     * @param key String the value to retrieve (e.g., {@code "shapeTypes"}, {@code "city"}, or {@code "latitude"})
     * @param <T> T the expected type of the returned value
     *
     * @return the associated value to the specified key
     */
    @SuppressWarnings("unchecked")
    public <T> T getLocation(final String key) {
        final Map<String, Object> location = getUser().getAdditional(LOCATION_FIELD);
        return location == null ? null : (T) location.get(key);
    }

    /**
     *
     * @param key    String, contains the key
     * @param value  Object, contains the value
     * @return
     */
    public BreinTemporalData setLocation(final String key, final Object value) {
        Map<String, Object> location = getUser().getAdditional(LOCATION_FIELD);
        if (location == null) {
            location = new HashMap<>();
            getUser().setAdditional(LOCATION_FIELD, location);
        }

        location.put(key, value);

        return this;
    }

    /**
     * Creates the signature for temporaldata
     *
     * @param config      BreinConfig configuration
     * @param requestData Map of String-Objects
     * @return signature
     */
    @Override
    public String createSignature(final BreinConfig config, final Map<String, Object> requestData) {
        final String localDateTime = BreinMapUtil.getNestedValue(requestData,
                BreinUser.USER_FIELD, BreinUser.ADDITIONAL_FIELD, LOCAL_DATE_TIME_FIELD);
        final String paraLocalDateTime = localDateTime == null ? "" : localDateTime;

        final String timeZone = BreinMapUtil.getNestedValue(requestData,
                BreinUser.USER_FIELD, BreinUser.ADDITIONAL_FIELD, TIMEZONE_FIELD);
        final String paraTimezone = timeZone == null ? "" : timeZone;

        final long unixTimestamp = BreinMapUtil.getNestedValue(requestData, UNIX_TIMESTAMP_FIELD);
        final String message = String.format("%d-%s-%s", unixTimestamp, paraLocalDateTime, paraTimezone);

        return BreinUtil.generateSignature(message, config.getSecret());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(ICallback callback) {
        Breinify.temporalData(this, callback);
    }

}


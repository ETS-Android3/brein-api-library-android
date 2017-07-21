package com.brein.domain.results;

import com.brein.domain.BreinResult;
import com.brein.domain.results.temporaldataparts.BreinEventResult;
import com.brein.domain.results.temporaldataparts.BreinHolidayResult;
import com.brein.domain.results.temporaldataparts.BreinLocationResult;
import com.brein.domain.results.temporaldataparts.BreinWeatherResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreinTemporalDataResult extends BreinResult {

    private static final String WEATHER_KEY = "weather";
    private static final String TIME_KEY = "time";
    private static final String TIMEZONE_KEY = "timezone";
    private static final String LOCAL_TIME_KEY = "localFormatIso8601";
    private static final String EPOCH_TIME_KEY = "epochFormatIso8601";
    private static final String LOCATION_KEY = "location";
    private static final String HOLIDAY_LIST_KEY = "holidays";
    private static final String EVENT_LIST_KEY = "events";

    public BreinTemporalDataResult(final Map<String, Object> json) {
        super(json == null ? new HashMap<String, Object>() : json);
    }

    public BreinTemporalDataResult(final BreinResult result) {
        this(result.getMap());
    }

    public boolean hasWeather() {
        return getValue(WEATHER_KEY) != null;
    }

    @SuppressWarnings("unchecked")
    public BreinWeatherResult getWeather() {
        return new BreinWeatherResult((Map<String, Object>) getValue(WEATHER_KEY));
    }

    public boolean hasLocalDateTime() {
        return getNestedValue(TIME_KEY, LOCAL_TIME_KEY) != null;
    }

    /*
    public ZonedDateTime getZonedDateTime() {
        final String value = getNestedValue(TIME_KEY, LOCAL_TIME_KEY);
        if (value == null) {
            return null;
        } else {
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            final String timezone = getNestedValue(TIME_KEY, TIMEZONE_KEY);
            if (timezone == null || timezone.isEmpty()) {
                return zonedDateTime;
            } else {
                try {
                    final ZoneId zoneId = ZoneId.of(timezone);
                    return zonedDateTime.withZoneSameInstant(zoneId);
                } catch (final Exception e) {
                    return zonedDateTime;
                }
            }
        }
    }
    */

    public boolean hasEpochDateTime() {
        return getNestedValue(TIME_KEY, EPOCH_TIME_KEY) != null;
    }

    /*
    public LocalDateTime getEpochDateTime() {
        final String value = getNestedValue(TIME_KEY, EPOCH_TIME_KEY);
        if (value == null) {
            return null;
        } else {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }
    */

    public boolean hasLocation() {
        return getValue(LOCATION_KEY) != null;
    }

    @SuppressWarnings("unchecked")
    public BreinLocationResult getLocation() {
        return new BreinLocationResult((Map<String, Object>) getValue(LOCATION_KEY));
    }

    public boolean hasHolidays() {
        return getValue(HOLIDAY_LIST_KEY) != null;
    }

    public List<BreinHolidayResult> getHolidays() {
        final List<Map<String, Object>> value = getValue(HOLIDAY_LIST_KEY);
        if (value == null) {
            return null;
        } else {
            // TODO: check implementation

            final List<BreinHolidayResult> breinHolidayResultList = new ArrayList<>();

            for (Map<String, Object> map : value) {

                // create new BreinHolidayResult
                final BreinHolidayResult breinHolidayResult = new BreinHolidayResult(map);
                breinHolidayResultList.add(breinHolidayResult);

                /*
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object object = entry.getValue();

                    // create new BreinHolidayResult
                    // final BreinHolidayResult breinHolidayResult = new BreinHolidayResult();

                }
                */

            }


            return breinHolidayResultList;

            /*
            return value.stream()
                    .map(BreinHolidayResult::new)
                    .collect(Collectors.toList());
                    */
        }
    }

    public boolean hasEvents() {
        return getValue(EVENT_LIST_KEY) != null;
    }

    public List<BreinEventResult> getEvents() {
        final List<Map<String, Object>> value = getValue(EVENT_LIST_KEY);
        if (value == null) {
            return null;
        } else {

            // TODO: check implementation

            final List<BreinEventResult> breinEventResultList = new ArrayList<>();

            for (Map<String, Object> map : value) {

                // create new
                final BreinEventResult breinEventResult = new BreinEventResult(map);

                breinEventResultList.add(breinEventResult);

                /*
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object object = entry.getValue();

                    // create new BreinHolidayResult
                    // final BreinHolidayResult breinHolidayResult = new BreinHolidayResult();

                }
                */

            }

            return breinEventResultList;


            /*
            return value.stream()
                    .map(BreinEventResult::new)
                    .collect(Collectors.toList());
                    */
        }
    }

    @Override
    public String toString() {
        return "Temporal results with " + (hasWeather() ? getWeather() : "no weather info");
    }

}

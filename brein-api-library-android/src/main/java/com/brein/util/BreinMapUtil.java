package com.brein.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreinMapUtil {


    /**
     * Map Helper method used to copy a hashmap of type String, Object
     *
     * @param source contains the original map
     *
     * @return a copy of the map or null if source is null
     */
    public static Map<String, Object> copyMap(final Map<String, Object> source) {
        if (source == null) {
            return null;
        }

        final Map<String, Object> copy = new HashMap<>();

        //loop a Map
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            // Todo: remove after check
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            copy.put(entry.getKey(), copyValue(entry.getValue()));
        }

        return copy;
    }

    public static List<Object> copyList(final List<Object> source) {
        if (source == null) {
            return null;
        }

        final List<Object> copy = new ArrayList<>();
        for (Object value : copy) {
            // Todo: remove
            System.out.println(value);
            copy.add(copyValue(value));
        }

        return copy;
    }

    @SuppressWarnings("unchecked")
    public static Object copyValue(final Object value) {
        if (List.class.isInstance(value)) {
            return BreinMapUtil.copyList(List.class.cast(value));
        } else if (Map.class.isInstance(value)) {
            return BreinMapUtil.copyMap(Map.class.cast(value));
        } else {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getNestedValue(final Map<String, Object> map, final String... keys) {
        if (map == null) {
            return null;
        }

        Map<String, Object> currentMap = map;

        Object value = null;
        int i = 0;
        for (; i < keys.length; i++) {
            final String k = keys[i];

            value = currentMap.get(k);
            if (value == null) {
                break;
            } else if (Map.class.isInstance(value)) {
                currentMap = Map.class.cast(value);
            } else if (i < keys.length - 1) {
                break;
            }
        }

        if (i == keys.length) {
            return (T) value;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean hasNestedValue(final Map<String, Object> map, final String... keys) {
        if (map == null) {
            return false;
        }

        Map<String, Object> currentMap = map;

        int i = 0;
        for (; i < keys.length; i++) {
            final String k = keys[i];

            final Object value = currentMap.get(k);
            if (value == null) {
                break;
            } else if (Map.class.isInstance(value)) {
                currentMap = Map.class.cast(value);
            } else if (i < keys.length - 1) {
                break;
            }
        }

        return i == keys.length;
    }
}

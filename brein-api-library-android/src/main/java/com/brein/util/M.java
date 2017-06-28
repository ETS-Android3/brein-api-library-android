package com.brein.util;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class M<T> {
    protected final Map<String, T> map = new HashMap<>();

    public static <T> M<T> fromMap(final Map<String, T> map) {
        final M<T> result = new M<>();
        return result.set(map);
    }

    public M<T> set(final String key, final T value) {
        map.put(key, value);

        return this;
    }

    @SuppressWarnings("unchecked")
    public M<T> set(final Map map) {

        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            set((String)pair.getKey(), (T) pair.getValue());
        }

       //  map.forEach((key, value) -> set(String.valueOf(key), (T) value));

        return this;
    }

    public M<T> set(final M map) {
        return set(map.map);
    }

    public Map<String, T> asMap() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /*
    public void forEach(final BiConsumer<String, T> action) {
        map.forEach(action);
    }
    */
}

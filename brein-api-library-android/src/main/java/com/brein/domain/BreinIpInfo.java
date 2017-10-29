package com.brein.domain;


import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * request: http://www.ip-api.com/json
 * <p>
 * response:
 * {
 * as: "AS3320 Deutsche Telekom AG",
 * city: "Roetgen",
 * country: "Germany",
 * countryCode: "DE",
 * isp: "Deutsche Telekom AG",
 * lat: 50.65,
 * lon: 6.2,
 * org: "Deutsche Telekom AG",
 * query: "217.247.43.188",
 * region: "NW",
 * regionName: "North Rhine-Westphalia",
 * status: "success",
 * timezone: "Europe/Berlin",
 * zip: "52159"
 * }
 */

public class BreinIpInfo {

    public static final String IP_FIELD = "query";
    public static final String TIMEZONE_FIELD = "timezone";

    // used for singleton
    private static volatile BreinIpInfo breinIpInfoInstance;

    private Map<String, Object> infoMap;

    // private constructor
    private BreinIpInfo() {
        // Prevent form the reflection api.
        if (breinIpInfoInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        refreshData();
    }

    public static BreinIpInfo getInstance() {
        // Double check locking pattern
        if (breinIpInfoInstance == null) { //Check for the first time

            synchronized (BreinIpInfo.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (breinIpInfoInstance == null) {
                    breinIpInfoInstance = new BreinIpInfo();
                }
            }
        }
        return breinIpInfoInstance;
    }

    @SuppressWarnings("unchecked")
    public void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String ipJson = invokeRequest();
                if (ipJson != null) {
                    infoMap = new Gson().fromJson(ipJson, Map.class);
                }
            }
        }).start();
    }

    public String invokeRequest() {
        HttpURLConnection urlConnection = null;
        try {
            final URL url = new URL("http://www.ip-api.com/json");

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            final InputStream in = urlConnection.getInputStream();
            final InputStreamReader isw = new InputStreamReader(in);

            String retVal = "";
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                retVal += Character.toString(current);
            }

            return retVal;
        } catch (final Exception e) {
            Log.e("BreinIpInfo", "Exception occured", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public String getExternalIp() {
        return infoMap == null ? null : (String) infoMap.get(IP_FIELD);
    }

    public String getTimezone() {
        return infoMap == null ? null : (String) infoMap.get(TIMEZONE_FIELD);
    }

}

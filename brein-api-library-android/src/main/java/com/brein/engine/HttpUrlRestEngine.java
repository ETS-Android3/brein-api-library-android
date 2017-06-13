package com.brein.engine;

import com.brein.api.BreinActivity;
import com.brein.api.BreinBase;
import com.brein.api.BreinException;
import com.brein.api.BreinLookup;
import com.brein.api.Breinify;
import com.brein.api.ICallback;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.util.BreinUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * could be the jersey rest com.brein.engine implementation
 */
public class HttpUrlRestEngine implements IRestEngine {

    /**
     * constant for post method
     */
    public final static String POST_METHOD = "POST";

    /**
     * invokes the post request. Needs to run a thread.
     *
     * @param breinActivity data
     */
    @Override
    public void doRequest(final BreinActivity breinActivity) {

        // validate the input objects
        BreinUtil.validate(breinActivity);

        final String fullUrl = BreinUtil.getFullyQualifiedUrl(breinActivity);
        final String requestBody = BreinUtil.getRequestBody(breinActivity);
        System.out.println("Request is: " + requestBody);
        final int connectionTimeout = (int) Breinify.getConfig().getConnectionTimeout();
        final int readTimeout = (int) Breinify.getConfig().getSocketTimeout();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(fullUrl);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(readTimeout);
                    conn.setConnectTimeout(connectionTimeout);
                    conn.setRequestMethod(POST_METHOD);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // final String data = requestObject.toString();
                    final PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(requestBody);
                    out.close();

                    conn.connect();
                    final int response = conn.getResponseCode();
                    System.out.println("response is: " + response);

                } catch (final Exception e) {
                    // System.out.println("Exception is: " + e);
                    throw new BreinException("REST rest call exception");
                }

            }
        }).start();
    }

    /**
     * performs a lookup and provides details
     *
     * @param breinLookup contains request data
     * @return response from Breinify
     */
    @Override
    public BreinResult doLookup(final BreinLookup breinLookup) {

        // validate the input objects
        BreinUtil.validate(breinLookup);

        final String fullUrl = BreinUtil.getFullyQualifiedUrl(breinLookup);
        final String requestBody = BreinUtil.getRequestBody(breinLookup);
        final int connectionTimeout = (int) Breinify.getConfig().getConnectionTimeout();
        final int readTimeout = (int) Breinify.getConfig().getSocketTimeout();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(fullUrl);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(readTimeout);
                    conn.setConnectTimeout(connectionTimeout);
                    conn.setRequestMethod(POST_METHOD);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // final String data = requestObject.toString();

                    final PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(requestBody);
                    out.close();

                    conn.connect();
                    final int response = conn.getResponseCode();
                    // System.out.println("response code is: " + response);

                    if (response == 200) {
                        String jsonString = "";

                        final InputStream mInputStream = conn.getInputStream();

                        int i = 0;
                        while ((i = mInputStream.read()) != -1) {
                            jsonString += (char) i;
                        }
                        // System.out.println("Response data is: " + jsonString);
                        // return new BreinResult(response.getEntity(String.class));

                    } else {
                        throw new BreinException("REST rest call exception");
                    }

                } catch (final Exception e) {
                    throw new BreinException("REST rest call exception");
                }

            }
        }).start();

        return null;
    }

    /**
     * stops possible functionality (e.g. threads)
     */
    @Override
    public void terminate() {
    }

    @Override
    public IRestEngine getRestEngine(BreinEngineType engine) {
        return null;
    }

    @Override
    public BreinEngineType getRestEngineType(BreinEngineType engine) {
        return null;
    }

    /**
     * configuration of the rest  client
     */
    @Override
    public void configure(final BreinConfig breinConfig) {
    }


    @Override
    public void invokeRequest(final BreinConfig config, final BreinBase data, final ICallback<BreinResult> callback) {

        // validate the input objects
        BreinUtil.validate(data);

        final String fullUrl = BreinUtil.getFullyQualifiedUrl(data);
        final String requestBody = BreinUtil.getRequestBody(data);
        System.out.println("Request is: " + requestBody);
        final int connectionTimeout = (int) Breinify.getConfig().getConnectionTimeout();
        final int readTimeout = (int) Breinify.getConfig().getSocketTimeout();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final URL url = new URL(fullUrl);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(readTimeout);
                    conn.setConnectTimeout(connectionTimeout);
                    conn.setRequestMethod(POST_METHOD);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");

                    // final String data = requestObject.toString();
                    final PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(requestBody);
                    out.close();

                    conn.connect();
                    final int response = conn.getResponseCode();
                    System.out.println("response is: " + response);
                    BreinResult breinResponse = null;
                    Map<String, Object> mapResponse = new HashMap<String, Object>();
                    mapResponse.put("Response", response);
                    if (response == 200) {
                        System.out.println("response is: " + conn.getResponseMessage());
                        // mapResponse = new Gson().fromJson(conn.getResponseMessage(), Map.class);
                        mapResponse.put("Message", conn.getResponseMessage());
                        breinResponse = new BreinResult(mapResponse);
                    }

                    conn.disconnect();

                    callback.callback(breinResponse);

                } catch (final Exception e) {
                    // System.out.println("Exception is: " + e);
                    throw new BreinException("REST rest call exception");
                }
            }
        }).start();
    }

}

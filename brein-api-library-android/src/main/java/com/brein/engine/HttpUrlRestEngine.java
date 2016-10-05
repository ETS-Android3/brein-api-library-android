package com.brein.engine;

import com.brein.api.BreinActivity;
import com.brein.api.BreinException;
import com.brein.api.BreinLookup;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.util.BreinUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * could be the jersey rest com.brein.engine implementation
 */
public class HttpUrlRestEngine implements IRestEngine {

    /**
     * Logger
     */
    // private static final Logger LOG = LoggerFactory.getLogger(JerseyRestEngine.class);

    /**
     * header info
     */
    private static final String HEADER_APP_JSON = "application/json";

    /**
     * create HttpURLConnection client
     */
    private HttpURLConnection conn;

    /**
     * invokes the post request
     *
     * @param breinActivity data
     */
    @Override
    public void doRequest(final BreinActivity breinActivity) {

        // validate the input objects
        BreinUtil.validate(breinActivity);

        final String fullUrl = BreinUtil.getFullyQualifiedUrl(breinActivity);
        final String requestBody = BreinUtil.getRequestBody(breinActivity);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(fullUrl);

                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // final String data = requestObject.toString();

                    final PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(requestBody);
                    out.close();

                    conn.connect();
                    int response = conn.getResponseCode();
                    // System.out.println("response is: " + response);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(fullUrl);

                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // final String data = requestObject.toString();

                    final PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(requestBody);
                    out.close();

                    conn.connect();
                    int response = conn.getResponseCode();
                    // System.out.println("response code is: " + response);

                    if (response == 200) {
                        String jsonString = "";

                        InputStream mInputStream = conn.getInputStream();

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
                    // System.out.println("Exception is: " + e);
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

    /**
     * configuration of the rest  client
     */
    @Override
    public void configure(final BreinConfig breinConfig) {
    }

}

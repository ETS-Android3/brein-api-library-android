package com.brein.util;

import android.util.Log;

import com.google.gson.Gson;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Ignore
public class TestUtil {

    @Test
    public void testSignature() {

        assertEquals("h5HRhGRwWlRs9pscyHhQWNc7pxnDOwDZBIAnnhEQbrU=",
                BreinUtil.generateSignature("apiKey", "secretkey"));

        final String secret = BreinUtil.generateSecret(128);
        for (int i = 0; i < 100; i++) {

            // this is our test case, for the message 'apiKey' and secret 'secretKey'
            // it must return the used signature, the message should vary on each
            // request
            BreinUtil.generateSignature(BreinUtil.randomString(), secret);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIpAddress() {

        final String extIp2 = getExternalIp2();
        Log.d("ExIP is:", extIp2);

        final String extIp = getExternalIpAsJson();
        Log.d("ExIP is:", extIp);

        Map<String,Object> result = new Gson().fromJson(extIp, Map.class);
        Log.d("Map is: ", result.toString());

        final String ip1 = getLocalIpAddress();
        Log.d("IP1 is: ", ip1);

        final String ip2 = getLocalIpAddress2();
        Log.d("IP2 is: ", ip2);

        // final String s = BreinUtil.detectIpAddress();
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    private String getLocalIpAddress2() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private String getExternalIp2() {

        try {
            final URL url = new URL("http://www.ip-api.com/json");
            final InputStream is = url.openStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();
            is.close();

            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getExternalIpAsJson() {

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            // url = new URL("http://ifcfg.me/ip");
            url = new URL("http://www.ip-api.com/json");

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            String retVal = "";
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
                retVal += current;
            }

            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }


}

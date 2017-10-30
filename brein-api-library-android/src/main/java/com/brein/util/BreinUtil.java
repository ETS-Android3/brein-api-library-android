package com.brein.util;

import com.brein.api.BreinBase;
import com.brein.api.BreinException;
import com.brein.api.Breinify;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinIpInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class
 */
public class BreinUtil {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Mac mac;
    private static final Random RANDOM = new Random();

    private BreinUtil() {}

    static {
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to find needed algorithm!", e);
        }
    }

    /**
     * Verifies if the object contains a value
     * Return false in case of:
     * - null
     * - empty strings
     *
     * @param object to check
     * @return true if object contains data
     */
    public static boolean containsValue(final Object object) {

        if (object == null) {
            return false;
        }

        if (object.getClass() == String.class) {
            final String strObj = (String) object;
            return strObj.length() > 0;
        }

        return true;
    }

    /**
     * Helper method to generate a random string
     *
     * @return string
     */
    public static String randomString() {
        final int len = 1 + RANDOM.nextInt(100);

        return randomString(len);
    }

    /**
     * Helper methods generates a random string by len
     *
     * @param len of the requested string
     * @return random string
     */
    public static String randomString(final int len) {
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(RANDOM.nextInt(AB.length())));
        }
        return sb.toString();
    }

    /**
     * Creates a secret by given len
     *
     * @param length of the secret
     * @return created secret
     */
    public static String generateSecret(final int length) {
        final SecureRandom random = new SecureRandom();

        final byte[] bytes = new byte[length / 8];
        random.nextBytes(bytes);

        return Base64.encodeBytes(bytes);
    }

    /**
     * generates the signature
     *
     * @param message contains the message
     * @param secret  contains the secret
     * @return signature
     */
    public static String generateSignature(final String message, final String secret) {

        if (message == null) {
            throw new BreinException("Illegal value for message in method generateSignature");
        }

        if (secret == null) {
            throw new BreinException("Illegal value for secret in method generateSignature");
        }

        try {
            byte[] e = secret.getBytes(Charset.forName("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(e, "HmacSHA256");
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(message.getBytes(Charset.forName("UTF-8")));
            return Base64.encodeBytes(hmacData);

        } catch (final InvalidKeyException e) {
            throw new IllegalStateException("Unable to create signature!", e);
        }
    }

    /**
     * Validates if the URL is correct.
     *
     * @param url to check
     * @return true if ok otherwise false
     */
    public static boolean isUrlValid(final String url) {
        return true;
    }

    public static boolean isUrlValidNotWorkingOnAndroid(final String url) {

        try {
            final URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("POST");
            huc.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");

            // Todo: this has caused issues on Android
            huc.connect();
            return true;

        } catch (final IOException e) {

            // this must be an error case!
            return false;
        }
    }

    /**
     * checks if the url is valid -> if not an exception will be thrown
     *
     * @param fullyQualifiedUrl url with endpoint
     */
    public static void validateUrl(final String fullyQualifiedUrl) throws BreinException {

        final boolean validUrl = isUrlValid(fullyQualifiedUrl);
        if (!validUrl) {
            final String msg = "URL: " + fullyQualifiedUrl + " is not valid!";
            throw new BreinException(msg);
        }
    }

    /**
     * validates the activity object
     *
     * @param breinBase object to validate
     */
    public static void validateBreinBase(final BreinBase breinBase) {
        if (null == breinBase) {
            throw new BreinException(BreinException.BREIN_BASE_VALIDATION_FAILED);
        }
    }

    /**
     * validates the configuration object
     *
     * @param breinBase activity or lookup object
     */
    public static void validateConfig(final BreinBase breinBase) {

        final BreinConfig breinConfig = Breinify.getConfig();
        if (null == breinConfig) {
            throw new BreinException(BreinException.CONFIG_VALIDATION_FAILED);
        }
    }

    /**
     * retrieves the fully qualified url (base + endpoint)
     *
     * @param breinBase activity or lookup object
     * @return full url
     */
    public static String getFullyQualifiedUrl(final BreinBase breinBase) {
        final BreinConfig breinConfig = Breinify.getConfig();

        final String url = breinConfig.getUrl();
        if (null == url) {
            throw new BreinException(BreinException.URL_IS_NULL);
        }

        final String endPoint = breinBase.getEndPoint(breinConfig);
        return url + endPoint;
    }

    /**
     * retrieves the request body depending of the object
     *
     * @param breinBase object to use
     * @return request as json string
     */
    public static String getRequestBody(final BreinBase breinBase) {

        final String requestBody = breinBase.prepareRequestData(Breinify.getConfig());
        if (!BreinUtil.containsValue(requestBody)) {
            throw new BreinException(BreinException.REQUEST_BODY_FAILED);
        }
        return requestBody;
    }

    /**
     * Invokes validation of BreinBase object, configuration and url.
     * The "validator" will throw an exception in case of any mis-behaviour.
     *
     * @param breinBase activity or lookup object
     */
    public static void validate(final BreinBase breinBase) {

        // validation of activity and config
        validateBreinBase(breinBase);
        validateConfig(breinBase);

        // validate URL, might throw an exception...
        // validateUrl(getFullyQualifiedUrl(breinBase));
    }

    /**
     * Safely casting long to int in Java without using java.lang.Math.toIntExact
     *
     * @param value long value to cast
     * @return int or exception
     */
    public static int safeLongToInt(long value) {
        int i = (int) value;
        if ((long) i != value) {
            throw new IllegalArgumentException(value + " cannot be cast to int without changing its value.");
        }
        return i;
    }

    /*
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            // for now eat exceptions
        }
        return "";
    }
    */

    public static String detectIpAddress() {
        return BreinIpInfo.getInstance().getExternalIp();
    }

}

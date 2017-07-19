package com.brein.config;

import com.brein.domain.BreinConfig;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test of configuration
 */
@Ignore
public class TestConfig {
    private static final String testApiKey = "TEST-API-KEY";

    /**
     * Init part
     */
    @BeforeClass
    public static void setUp() {
    }

    /**
     * Test a config with a wrong url
     */
    //@Test(expected= BreinInvalidConfigurationException.class)
    public void testConfigWithWrongUrl() {

        final String wrongUrl = "https://breeeeeinify.com";
        final BreinConfig breinConfig = new BreinConfig(testApiKey)
                .setBaseUrl(wrongUrl);

        final boolean isValid = breinConfig.isUrlValid(wrongUrl);
        assertFalse(isValid);
    }

    /**
     * Test a config with a correct url
     */
    @Test
    public void testConfigWithCorrectUrl() {

        final String correctUrl = "http://google.com";

        final BreinConfig breinConfig = new BreinConfig(testApiKey)
                .setBaseUrl(correctUrl);

        final boolean isValid = breinConfig.isUrlValid(correctUrl);
        assertTrue(isValid);
    }

    /**
     * Tests if both Breinify URL's are reachable:
     *  https://api.breinify.com
     *  http://api.breinify.com
     */
    @Test
    public void testBreinifyUrls() {

        final String httpsUrl = "https://api.breinify.com";
        final String httpUrl = "http://api.breinify.com";

        // HTTPS
        final BreinConfig breinConfigHttps = new BreinConfig(testApiKey)
                .setBaseUrl(httpsUrl);
        final boolean isHttpsValid = breinConfigHttps.isUrlValid(httpsUrl);
        assertTrue(isHttpsValid);

        // HTTP
        final BreinConfig breinConfigHttp = new BreinConfig(testApiKey)
                .setBaseUrl(httpUrl);

        final boolean isHttpValid = breinConfigHttps.isUrlValid(httpUrl);
        assertTrue(isHttpValid);
    }

}


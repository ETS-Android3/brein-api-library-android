package com.brein.api;

import com.brein.domain.BreinActivityType;
import com.brein.domain.BreinCategoryType;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.engine.BreinEngine;
import com.brein.engine.BreinEngineType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of Breinify Java API (static option)
 */
// @Ignore
public class TestHttpUrlConnectionApi {

    /**
     * Contains the BASE URL of the Breinify Backend
     */
    private static final String BASE_URL = "https://api.breinify.com";

    /**
     * This has to be a valid api key
     */
    private static final String VALID_API_KEY = "41B2-F48C-156A-409A-B465-317F-A0B4-E0E8";
    private static final String VALID_SECRET = "djfsdkfdfskjfksdjkf";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("toni.tester@mail.net");

    /**
     * Contains the Category
     */
    private final String breinCategoryType = "home";

    /**
     * Correct configuration
     */
    final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);

    private final RestCallback restCallback = new RestCallback();

    /**
     * Init part
     */
    @BeforeClass
    public static void init() {
    }

    /**
     * setup for each test
     */
    @Before
    public void setUp() {
    }

    /**
     * Housekeeping...
     */
    @AfterClass
    public static void tearDown() {
        try {
            Thread.sleep(4000);
            Breinify.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void wait5Seconds() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * testcase how to use the activity api
     */
    @Test
    public void testLogin() {

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * additional optional user information
         */
        breinUser.setFirstName("Marco");
        breinUser.setLastName("Recchioni");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                BreinCategoryType.HOME,
                "Login-Description",
                restCallback
                );
    }

    /**
     * Testcase with null value as apikey
     */
    @Test
    public void testLoginWithNullApiKey() {

        final String description = "Login-Description";

        final BreinConfig config = new BreinConfig(null, VALID_SECRET);
        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("Marco");
        breinUser.setLastName("Recchioni");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * Testcase with null value as base url
     */
    @Test
    public void testLoginWithoutSecret() {

        final String description = "Login-Description";
        final boolean sign = false;

        final BreinConfig config = new BreinConfig(VALID_API_KEY, null);

        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("Marco");
        breinUser.setLastName("Recchioni");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * Testcase with null rest engine. This will throw an
     * exception.
     */
    @Test(expected= BreinException.class)
    public void testLoginWithNoRestEngine() {

        final String description = "Login-Description";
        final boolean sign = false;

        BreinConfig config = null;
        try {
            config = new BreinConfig(VALID_API_KEY, VALID_SECRET, BreinEngineType.NO_ENGINE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("Marco");
        breinUser.setLastName("Recchioni");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * Test case with wrong endpoint configuration
     */
    @Test
    public void testWithWrongEndPoint() {

        final String description = "Login-Description";

        /*
         * set configuration
         */
        BreinConfig config = new BreinConfig(VALID_API_KEY, VALID_SECRET);
        config.setActivityEndpoint("/wrongEndPoint");

        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("Marco");
        breinUser.setLastName("Recchioni");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                breinCategoryType,
                description,restCallback);
    }

    /**
     * Invoke a test call with 20 logins
     */
    @Test
    public void testWith20Logins() {

        final int maxLogin = 20;

        for (int index = 0; index < maxLogin; index++) {
            testLogin();
        }
    }

    /**
     * test case how to invoke logout activity
     */
    @Test
    public void testLogout() {

        final String description = "Logout-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * additional user information
         */
        breinUser.setDateOfBirth(12, 31, 2008);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.LOGOUT,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * testcase
     */
    @Test
    public void testSearch() {

        final String description = "Search-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.SEARCH,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * testcase
     */
    @Test
    public void testAddToCart() {

        final String description = "AddToCart-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.ADD_TO_CART,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * testcase
     */
    @Test
    public void testRemoveFromCart() {

        final String description = "RemoveFromCart-Description";

        Breinify.setConfig(breinConfig);

        Breinify.activity(breinUser,
                BreinActivityType.REMOVE_FROM_CART,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * testcase
     */
    @Test
    public void testSelectProduct() {

        final String description = "Select-Product-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.SELECT_PRODUCT,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * testcase
     */
    @Test
    public void testOther() {

        final String description = "Other-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.OTHER,
                breinCategoryType,
                description, restCallback);
    }

    /**
     * simply demonstrate the configuration of the engine
     */
    @Test
    public void testConfiguration() {

        final BreinEngine breinEngine = breinConfig.getBreinEngine();

        /*
         * set connection timeout to 30000 ms
         */
        breinConfig.setConnectionTimeout(30000);

        /*
         * set socket timeout to 25000 ms
         */
        breinConfig.setSocketTimeout(25000);

        /*
         * configure the engine
         */
        breinEngine.configure(breinConfig);
    }



}

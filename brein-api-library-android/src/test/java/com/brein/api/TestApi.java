package com.brein.api;

import com.brein.domain.BreinActivityType;
import com.brein.domain.BreinCategoryType;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.domain.results.BreinTemporalDataResult;
import com.brein.engine.BreinEngine;
import com.brein.engine.BreinEngineType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Test of Breinify Java API (static option)
 */

@Ignore
public class TestApi {

    /**
     * Contains the BASE URL of the Breinify Backend
     */
    private static final String BASE_URL = "https://api.breinify.com";

    /**
     * This has to be a valid api key
     */
    private static final String VALID_SIGNATURE_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SIGNATURE = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("User.Name@email.com");

    /**
     * Contains the Category
     */
    private final String breinCategoryType = BreinCategoryType.HOME;

    /**
     * Contains the BreinActivityType
     */
    private final String breinActivityType = BreinActivityType.LOGIN;

    /**
     * Correct configuration
     */
    final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);

    /**
     * Catches the result from the rest call
     */
    class RestResult implements ICallback<BreinResult> {

        @Override
        public void callback(BreinResult data) {

            assertTrue(data != null);

            System.out.println("within RestResult");
            System.out.println("Data is: " + data.toString());

            for (Map.Entry<String, Object> entry : data.getMap().entrySet()) {
                System.out.println("entry: " + entry.getKey() + " - value: " + entry.getValue());

                final BreinTemporalDataResult temporalDataResult = new BreinTemporalDataResult(data);

                boolean hasWeather = temporalDataResult.hasWeather();
                boolean hasEvents = temporalDataResult.hasEvents();
                boolean hasLocalDateTime = temporalDataResult.hasLocalDateTime();
                boolean hasEpochDateTime = temporalDataResult.hasEpochDateTime();
                boolean hasHolidays = temporalDataResult.hasHolidays();
            }
        }
    }

    private final ICallback restCallback = new RestResult();

    /**
     * Init part
     */
    @BeforeClass
    public static void setUp() {
    }

    /**
     * Housekeeping...
     */
    @AfterClass
    public static void tearDown() {

        /*
         * we have to wait some time in order to allow the asynch rest processing
         */
        try {
            // TODO: 28/06/2017  remove this sleep statement
            Thread.sleep(5000);
            Breinify.shutdown();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void waitSomeSeconds() {
        try {
            Thread.sleep(8000);
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
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                breinActivityType,
                breinCategoryType,
                "Login-Description",
                restCallback);
    }

    /**
     * testcase without category type
     */
    @Test
    public void testWithoutCategoryType() {

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * additional optional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                breinActivityType,
                null,
                "Login-Description",
                restCallback);
    }

    /**
     * Testcase with null value as apikey
     */
    @Test
    public void testLoginWithNullApiKey() {

        final String description = "Login-Description";
        final boolean sign = false;

        final BreinConfig config = new BreinConfig(null, VALID_SIGNATURE);
        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("User")
                .setLastName("Name");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                breinActivityType,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * Testcase with null value as base url
     */
    @Test
    public void testWithoutSecret() {

        final BreinConfig config = new BreinConfig(VALID_SIGNATURE_API_KEY, null);

        Breinify.setConfig(config);
    }

    /**
     * Testcase with null rest engine. This will throw an
     * exception.
     */
    // @Test (expected = BreinException.class)
    public void testLoginWithDefaultRestEngine() {

        final String description = "Login-Description";
        final boolean sign = false;

        BreinConfig config = null;
        try {
            config = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE, BreinEngineType.NO_ENGINE);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                breinActivityType,
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
        final BreinConfig config = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);
        config.setActivityEndpoint("/wrongEndPoint");

        Breinify.setConfig(config);

        /*
         * additional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                breinActivityType,
                breinCategoryType,
                description,
                restCallback);
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
                breinActivityType,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * test case how to invoke search activity
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
                description,
                restCallback);
    }

    /**
     * test case how to invoke addToCart activity
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
                description,
                restCallback);
    }

    /**
     * test case how to invoke removeFromCart activity
     */
    @Test
    public void testRemoveFromCart() {

        final String description = "RemoveFromCart-Description";

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke activity call
         */
        Breinify.activity(breinUser,
                BreinActivityType.REMOVE_FROM_CART,
                breinCategoryType,
                description,
                restCallback);
    }

    /**
     * test case how to invoke selectProduct activity
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
                description,
                restCallback);
    }

    /**
     * test case how to invoke other activity
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
                description,
                restCallback);
    }

    /**
     * test case how to invoke it with flebile values
     */
    @Test
    public void testFlexibleValues() {

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
                description,
                restCallback);
    }

    /**
     * test case containing additional information
     */
    @Test
    public void testPageVisit() {

        // set configuration
        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);
        Breinify.setConfig(breinConfig);

        // user data
        final BreinUser breinUser = new BreinUser("User.Name@email.com")
                .setFirstName("Marco")
                .setLastName("Recchioni")
                .setDateOfBirth(11, 20, 1999)
                .setDeviceId("DD-EEEEE")
                .setImei("55544455333")
                .setSessionId("r3V2kDAvFFL_-RBhuc_-Dg")
                .setUrl("https://sample.com.au/home")
                .setReferrer("https://sample.com.au/track")
                .setIpAddress("10.11.12.130")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");

        final BreinActivity breinActivity = Breinify.getBreinActivity();

        // just in case you want to set the unixTimestamp
        breinActivity.setUnixTimestamp(System.currentTimeMillis() / 1000L);
        breinActivity.setUser(breinUser);
        breinActivity.setCategory(BreinCategoryType.APPAREL);
        breinActivity.setActivityType(BreinActivityType.PAGEVISIT);
        breinActivity.setDescription("your description");
        breinActivity.setTag("t1", 0.0);
        breinActivity.setTag("t2", 5);
        breinActivity.setTag("t3", "0.0");
        breinActivity.setTag("t4", 5.0000);
        breinActivity.setTag("nr", 3000);
        breinActivity.setTag("sortid", "1.0");

        Breinify.activity(breinActivity, restCallback);
    }

    /**
     * test case without having set the BreinUser.
     * This will lead to an Exception.
     */
    //@Test(expected= BreinException.class)
    public void testPageVisitWithException() {

        // set configuration
        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY,
                BASE_URL,
                BreinEngineType.HTTP_URL_CONNECTION_ENGINE);
        Breinify.setConfig(breinConfig);

        // user data
        final BreinUser breinUser = new BreinUser("User.Name@email.com")
                .setFirstName("User")
                .setLastName("Name")
                .setDateOfBirth(11, 20, 1999)
                .setDeviceId("DD-EEEEE")
                .setImei("55544455333")
                .setSessionId("r3V2kDAvFFL_-RBhuc_-Dg")
                .setUrl("https://sample.com.au/home")
                .setReferrer("https://sample.com.au/track")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");

        Breinify.activity(breinUser, BreinActivityType.PAGEVISIT, BreinCategoryType.APPAREL, "Description", restCallback);
    }

    /**
     * simply demonstrate the configuration of the engine
     */
    @Test
    public void testConfiguration() {

        final BreinEngine breinEngine = breinConfig.getBreinEngine();

        /*
         * set connection timeout to 10000 ms
         */
        breinConfig.setConnectionTimeout(10000);

        /*
         * set socket timeout to 10000 ms
         */
        breinConfig.setSocketTimeout(10000);

        /*
         * configure the engine
         */
        breinEngine.configure(breinConfig);
    }

    /**
     * Tests the lookup functionality
     */
    @Test
    public void testLookup() {

        final String[] dimensions = {"firstname",
                "gender",
                "age",
                "agegroup",
                "digitalfootprint",
                "images"};

        final BreinDimension breinDimension = new BreinDimension(dimensions);

        /*
         * set configuration
         */
        Breinify.setConfig(breinConfig);

        /*
         * invoke lookup
         */
        // TODO: 25.05.17 provide callback for result
        // final BreinResult response = Breinify.lookup(breinUser, breinDimension);

        /*
        if (response != null) {
            final Object dataFirstname = response.get("firstname");
            final Object dataGender = response.get("gender");
            final Object dataAge = response.get("age");
            final Object dataAgeGroup = response.get("agegroup");
            final Object dataDigitalFootprinting = response.get("digitalfootprint");
            final Object dataImages = response.get("images");
        }
        */
    }

    /**
     * Test a login activity with sign and correct secret
     */
    /**
     * Test a login activity with sign and correct secret
     */
    @Test
    public void testLoginWithSign() {

        final String secret = "lmcoj4k27hbbszzyiqamhg==";
        final boolean sign = true;

        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);

        // set secret
        breinConfig.setSecret(secret);

        // set config
        Breinify.setConfig(breinConfig);

        // invoke activity call
        Breinify.activity(breinUser,
                "login",
                "home",
                "Login-Description",
                restCallback);
    }

    /**
     * Test a login activity with sign but wrong secret
     */
    @Test
    public void testLoginWithSignButWrongSecret() {

        final String wrongSecret = "ThisIsAWrongSecret";

        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, wrongSecret);

        // set config
        Breinify.setConfig(breinConfig);

        // invoke activity call
        Breinify.activity(breinUser,
                BreinActivityType.LOGIN,
                BreinCategoryType.HOME,
                "Login-Description",
                restCallback);
    }


    /**
     * test case where an activity is sent without having set
     * the category type for this particular activity object.
     * In this case the default category type has to be used.
     * If this is not set then the call needs to be rejected.
     */
    @Test
    public void testActivityWithoutCategory() {

        // create configuration
        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE)
                .setDefaultCategory("DEF-CAT-TYPE");

        Breinify.setConfig(breinConfig);

        // create user
        final BreinUser breinUser = new BreinUser()
                .setSessionId("SESS-ID-IS-THIS");

        // send activity with all fields sets so far
        Breinify.activity(breinUser, "ACT-TYPE", "CAT-TYPE", "DESC", restCallback);

        // send activity without CAT-TYPE => use default
        Breinify.activity(breinUser, "ACT-TYPE", "", "DESC", restCallback);

        Breinify.activity(breinUser, "ACT-TYPE", null, "DESC", restCallback);

        Breinify.activity(breinUser, "ACT-TYPE", "bla", null, restCallback);

        Breinify.activity(breinUser, "ACT-TYPE", "bla", "Desc", null);

    }


    @Test
    public void testTemporalData() {
        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);
        Breinify.setConfig(breinConfig);

        final BreinUser breinUser = new BreinUser("fred.firestone@email.com")
                .setFirstName("Fred")
                .setIpAddress("74.115.209.58");
        //    .setTimezone("America/Los_Angeles")
        //    .setLocalDateTime("Sun Jul 2 2017 18:15:48 GMT-0800 (PST)");

        BreinTemporalData breinTemporalData = new BreinTemporalData()
                .setLocation("san francisco")
                .setUser(breinUser);

        breinTemporalData.execute(restCallback);
    }

    @Test
    public void testRecommendation() {

        final BreinConfig breinConfig = new BreinConfig(VALID_SIGNATURE_API_KEY, VALID_SIGNATURE);
        Breinify.setConfig(breinConfig);

        final BreinUser breinUser = new BreinUser()
                .setEmail("tester.breinify@email.com")
                .setSessionId("1133AADDDEEE");

        final int numberOfRecommendations = 3;
        final BreinRecommendation recommendation = new BreinRecommendation()
                .setUser(breinUser)
                .setNumberOfRecommendations(numberOfRecommendations);

        Breinify.recommendation(recommendation, restCallback);
    }


    @Test
    public void testForDoc() {

    }

}

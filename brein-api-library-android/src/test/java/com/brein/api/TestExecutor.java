package com.brein.api;

import com.brein.domain.BreinActivityType;
import com.brein.domain.BreinCategoryType;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.engine.BreinEngineType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

/**
 * Test of Breinify Java API (static option)
 */
@Ignore
public class TestExecutor {

    /**
     * some constants for lookup
     */
    private static final String FIRSTNAME = "firstname";
    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String AGEGROUP = "agegroup";
    private static final String DIGITALFOOTPRINT = "digitalfootprint";
    private static final String IMAGES = "images";

    /**
     * Contains the BASE URL of the Breinify Backend
     */
    private static final String BASE_URL = "https://api.breinify.com";

    /**
     * This has to be a valid api key
     */
    private static final String VALID_API_KEY = "41B2-F48C-156A-409A-B465-317F-A0B4-E0E8";

    private static final String VALID_SIGNATURE_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("User.Name@email.com");

    /**
     * Init part
     */
    @BeforeClass
    public static void init() {

        // set logging on
        final Properties props = System.getProperties();
        props.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");

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
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void wait4Seconds() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * testcase how to use the activity api
     */
    @Test
    public void testPageVisit() {

        /*
         * additional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");
        breinUser.setDateOfBirth(11, 20, 1999);
        breinUser.setDeviceId("DD-EEEEE");
        breinUser.setImei("55544455333");
        breinUser.setSessionId("r3V2kDAvFFL_-RBhuc_-Dg");
        breinUser.setUrl("https://sample.com.au/home");
        breinUser.setReferrer("https://sample.com.au/track");
        breinUser.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");

        final BreinifyExecutor breinifyExecutor = new BreinConfig()
                .setApiKey(VALID_API_KEY)
                .setBaseUrl(BASE_URL)
                .setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE)
                .build();

        final BreinActivity breinActivity = breinifyExecutor.getBreinActivity();

        breinActivity.setBreinUser(breinUser);
        breinActivity.setBreinCategoryType(BreinCategoryType.APPAREL);
        breinActivity.setBreinActivityType(BreinActivityType.PAGEVISIT);
        breinActivity.setDescription("your description");
        breinActivity.setSign(false);

        /*
         * invoke activity call
         */
        breinifyExecutor.activity();
    }

    /**
     * testcase how to use the activity api
     */
    @Test
    public void testLogin() {

        /*
         * additional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        final BreinifyExecutor breinifyExecutor = new BreinConfig()
                .setApiKey(VALID_API_KEY)
                .setBaseUrl(BASE_URL)
                .setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE)
                .build();

        final String description = "your description";
        /*
         * invoke activity call
         */
        breinifyExecutor.activity(breinUser,
                BreinActivityType.LOGIN,
                BreinCategoryType.FOOD,
                description,
                false);
    }

    /**
     * testcase how to use the activity api
     */
    @Test
    public void testWitoutCategorySet() {

        /*
         * additional user information
         */
        breinUser.setFirstName("User");
        breinUser.setLastName("Name");

        final BreinifyExecutor breinifyExecutor = new BreinConfig()
                .setApiKey(VALID_API_KEY)
                .setBaseUrl(BASE_URL)
                .setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE)
                .build();

        final String description = "your description";
        /*
         * invoke activity call
         */
        breinifyExecutor.activity(breinUser,
                BreinActivityType.LOGIN,
                null,
                description,
                false);
    }


    /**
     * Tests the lookup functionality
     */
    @Test
    public void testLookup() {

        final String[] dimensions = {
                FIRSTNAME,
                GENDER,
                AGE,
                AGEGROUP,
                DIGITALFOOTPRINT,
                IMAGES
        };

        final BreinDimension breinDimension = new BreinDimension(dimensions);
        final boolean sign = false;

        final BreinifyExecutor breinifyExecutor = new BreinConfig()
                .setApiKey(VALID_API_KEY)
                .setBaseUrl(BASE_URL)
                .setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE)
                .build();

        /*
         * use Philipp, so can get some data
         */
        final BreinUser user = new BreinUser("user.name@email.com");

        /*
         * invoke lookup
         */
        final BreinResult result = breinifyExecutor.lookup(user,
                breinDimension, sign);

        if (result != null) {
            final Object dataFirstname = result.get(FIRSTNAME);
            final Object dataGender = result.get(GENDER);
            final Object dataAge = result.get(AGE);
            final Object dataAgeGroup = result.get(AGEGROUP);
            final Object dataDigitalFootprinting = result.get(DIGITALFOOTPRINT);
            final Object dataImages = result.get(IMAGES);
        }

        // assertNotEquals(null, dataFirstname);
    }

    /**
     * Test activity call with bad url
     */
    // @Test(expected= BreinInvalidConfigurationException.class)
    public void testLoginWithBadUrl() {

        /*
         * Just to ensure that the right config has been set
         */
        final String badUrl = "www.beeeeeiiiniiify.com";

        final BreinifyExecutor breinifyExecutor = new BreinConfig()
                .setApiKey(VALID_API_KEY)
                .setBaseUrl(badUrl)
                .setRestEngineType(BreinEngineType.HTTP_URL_CONNECTION_ENGINE)
                .build();

        breinifyExecutor.activity(breinUser,
                BreinActivityType.LOGIN,
                BreinCategoryType.FOOD,
                "description",
                false);

    }

    @Test
    public void testForQA() {

    }

}

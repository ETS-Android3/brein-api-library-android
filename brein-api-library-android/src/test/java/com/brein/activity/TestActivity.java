package com.brein.activity;

import com.brein.api.BreinActivity;
import com.brein.api.Breinify;
import com.brein.api.ICallback;
import com.brein.domain.BreinActivityType;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static junit.framework.Assert.assertTrue;

/**
 * This test cases shows how to use the  activity
 */
@Ignore
public class TestActivity {

    /**
     * This has to be a valid api key & secret
     */
    private static final String VALID_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SECRET = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("Toni.Maroni@breinify.com");

    /**
     * Contains the Category
     */
    private final String breinCategory = "services";

    /**
     * The Activity itself
     */
    private final BreinActivity breinActivity = new BreinActivity();


    class RestResult implements ICallback<BreinResult> {

        @Override
        public void callback(BreinResult data) {

            assertTrue(data != null);

            System.out.println("within RestResult");
            System.out.println("Data is: " + data.toString());

        }
    }

    private final ICallback<BreinResult> restCallback = new RestResult();

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
     * Preparation of test case
     */
    @Before
    public void setUp() {

        final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);
        Breinify.setConfig(breinConfig);
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
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @After
    public void wait4FiveSeconds() {
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
         * additional user information
         */
        breinUser.setFirstName("Toni");
        breinUser.setLastName("Maroni");

        breinActivity.setUser(breinUser);
        breinActivity.setActivityType(BreinActivityType.LOGIN);
        breinActivity.setCategory(breinCategory);
        breinActivity.setDescription("Bla");

        breinActivity.execute(restCallback);

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
         * additional user information
         */
        breinUser.setDateOfBirth(12, 31, 2008);

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.LOGOUT,
                breinCategory, description, restCallback);
    }

    /**
     * test case how to invoke search activity
     */
    @Test
    public void testSearch() {

        final String description = "Search-Description";

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.SEARCH,
                breinCategory, description, restCallback);
    }

    /**
     * test case how to invoke add-to-cart activity
     */
    @Test
    public void testAddToCart() {

        final String description = "AddToCart-Description";

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.ADD_TO_CART,
                breinCategory, description, restCallback);
    }

    /**
     * test case how to invoke remove-from-cart activity
     */
    @Test
    public void testRemoveFromCart() {

        final String description = "RemoveFromCart-Description";

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.REMOVE_FROM_CART,
                breinCategory, description, restCallback);
    }

    /**
     * test case how to invoke select product
     */
    @Test
    public void testSelectProduct() {

        final String description = "Select-Product-Description";

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.SELECT_PRODUCT,
                breinCategory, description, restCallback);
    }

    /**
     * test case how to invoke other
     */
    @Test
    public void testOther() {

        final String description = "Other-Description";

        /*
         * invoke activity call
         */
        breinActivity.activity(breinUser,
                BreinActivityType.OTHER,
                breinCategory, description, restCallback);
    }

}

package com.brein.lookup;

import com.brein.api.BreinLookup;
import com.brein.api.Breinify;
import com.brein.api.RestCallback;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinUser;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Test cases for lookup functionality
 */
//@Ignore
public class TestLookup {

    /**
     * Contains the BASE URL of the Breinify Backend
     */
    private static final String BASE_URL = "https://api.breinify.com";

    /**
     * This has to be a valid api key & secret
     */
    private static final String VALID_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SECRET = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("toni.maroni@mail.net");

    /**
     * The Lookup itself
     */
    private final BreinLookup breinLookup = new BreinLookup();

    private final RestCallback restCallback = new RestCallback();

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
        /**
         * we have to wait some time in order to allow the asynch rest processing
         */
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            fail();
        }
    }

    /**
     * Tests the lookup functionality
     *
     */
    @Test
    public void testLookup() {

        final String[] dimensions = {"firstname", "gender",
                "age", "agegroup", "digitalfootprint", "images"};

        final BreinDimension breinDimension = new BreinDimension(dimensions);
        breinLookup.setBreinDimension(breinDimension);
        breinLookup.setUser(breinUser);

        /**
         * invoke lookup
         */
        breinLookup.execute(restCallback);


        /*
        if (breinResult != null) {
            final Object dataFirstname = breinResult.get("firstname");
            final Object dataGender = breinResult.get("gender");
            final Object dataAge = breinResult.get("age");
            final Object dataAgeGroup = breinResult.get("agegroup");
            final Object dataDigitalFootprinting = breinResult.get("digitalfootprint");
            final Object dataImages = breinResult.get("digitalfootprint");
        }
        */
    }
}

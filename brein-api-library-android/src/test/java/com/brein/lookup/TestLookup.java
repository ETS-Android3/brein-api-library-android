package com.brein.lookup;

import com.brein.api.BreinLookup;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinDimension;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.engine.BreinEngineType;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Test cases for lookup functionality
 */
// @Ignore
public class TestLookup {

    /**
     * Contains the BASE URL of the Breinify Backend
     */
    // private static final String BASE_URL = "http://dev.breinify.com/api";
    private static final String BASE_URL = "https://api.breinify.com";

    /**
     * This has to be a valid api key
     */
    private static final String VALID_API_KEY = "41B2-F48C-156A-409A-B465-317F-A0B4-E0E8";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("toni.maroni@mail.net");

    /**
     * The Lookup itself
     */
    private final BreinLookup breinLookup = new BreinLookup();

    /**
     * Preparation of test case
     */
    @Before
    public void setUp() {

        final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY);
        breinLookup.setConfig(breinConfig);
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
            /**
             * TODO...
             * Thread.sleep is not the best practice...
             *
             */
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

        /**
         * invoke lookup
         */
        final BreinResult breinResult = breinLookup.lookUp(breinUser, breinDimension);
        if (breinResult != null) {
            final Object dataFirstname = breinResult.get("firstname");
            final Object dataGender = breinResult.get("gender");
            final Object dataAge = breinResult.get("age");
            final Object dataAgeGroup = breinResult.get("agegroup");
            final Object dataDigitalFootprinting = breinResult.get("digitalfootprint");
            final Object dataImages = breinResult.get("digitalfootprint");
        }
    }
}

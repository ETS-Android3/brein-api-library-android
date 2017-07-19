package com.brein.engine;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestEngine {

    /**
     * This has to be a valid api key & secret
     */
    private static final String VALID_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SECRET = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Preparation for the test cases
     */
    @Before
    public void setUp() {
    }

    /**
     * Housekeeping...
     */
    @AfterClass
    public static void tearDown() {
    }

    /**
     * This should run some tests for the jersey client api...
     */
    @Test
    public void testJerseyRestEngine() {
    }

}

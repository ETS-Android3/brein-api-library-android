package com.brein.api;


import com.brein.domain.BreinActivityType;
import com.brein.domain.BreinCategoryType;
import com.brein.domain.BreinConfig;
import com.brein.domain.BreinUser;

import org.junit.AfterClass;
import org.junit.Test;

public class TestStress {

    /**
     * This has to be a valid api key
     */
    private static final String VALID_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SECRET = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Contains the Breinify User
     */
    private final BreinUser breinUser = new BreinUser("User.Name@email.com");

    /**
     * Correct configuration
     */
    final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);

    @AfterClass
    public static void tearDown() {

        /*
         * we have to wait some time in order to allow the asynch rest processing
         */
        try {
            Thread.sleep(10000);
            Breinify.shutdown();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testActivityStress() {

        // do it 200 times?
        int index = 0;
        do {
            new Thread(new Runnable() {
                public void run() {
                    testActivity();
                }
            }).start();
            index++;
            System.out.println("Thread " + index + " started");
        } while (index <= 2000);
    }

    private void testActivity() {

        Breinify.setConfig(breinConfig);

        breinUser.setFirstName("Toni");
        breinUser.setLastName("Maroni");

        Breinify.activity(breinUser,
                BreinActivityType.OTHER,
                BreinCategoryType.HOME,
                "Description",
                null
        );
    }
}

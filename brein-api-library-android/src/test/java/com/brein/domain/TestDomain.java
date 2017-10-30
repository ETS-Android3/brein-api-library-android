package com.brein.domain;

import com.brein.api.BreinActivity;
import com.brein.api.Breinify;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test classes for the domain objects
 */
@Ignore
public class TestDomain {

    private static final String VALID_API_KEY = "CA8A-8D28-3408-45A8-8E20-8474-06C0-8548";
    private static final String VALID_SECRET = "lmcoj4k27hbbszzyiqamhg==";

    /**
     * Init part
     */
    @BeforeClass
    public static void setUp() {
    }

    /**
     * creates a brein request object that will be used within the body
     * of the request
     */
    @Test
    public void testBreinRequest() {
        final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);
        final BreinUser breinUser = new BreinUser("toni.maroni@mail.com")
                .setFirstName("Toni")
                .setLastName("Maroni");

        final BreinActivity breinActivity = new BreinActivity();
        Breinify.setConfig(breinConfig);
        breinActivity.setUser(breinUser);
        breinActivity.setActivityType(BreinActivityType.LOGIN);
        breinActivity.setDescription("Super-Description");
        breinActivity.setCategory(BreinCategoryType.HOME);

        final String jsonOutput = breinActivity.prepareRequestData(breinConfig);
        assertTrue(jsonOutput.length() > 0);
    }

    /**
     * creates a brein request object that will be used within the body
     * of the request but with less data
     */
    @Test
    public void testBreinRequestWithLessData() {
        final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);
        final BreinUser breinUser = new BreinUser();
        final BreinActivity breinActivity = new BreinActivity();

        Breinify.setConfig(breinConfig);
        breinActivity.setUser(breinUser);
        breinActivity.setActivityType(BreinActivityType.LOGIN);
        breinActivity.setDescription("Super-Description");
        breinActivity.setCategory(BreinCategoryType.FOOD);

        final String jsonOutput = breinActivity.prepareRequestData(breinConfig);
        assertTrue(jsonOutput.length() > 0);
    }

    /**
     * Test the birthday settings
     */
    @Test
    public void testBirthday() {

        final BreinUser breinUser = new BreinUser();

        // set right values
        breinUser.setDateOfBirth(1, 22, 1966); // this is correct date
        assertFalse(breinUser.getDateOfBirth().isEmpty());

        // set wrong day
        breinUser.resetDateOfBirth();
        breinUser.setDateOfBirth(1, 77, 1966); // this is wrong date
        assertTrue(breinUser.getDateOfBirth().isEmpty());

        // set wrong month
        breinUser.resetDateOfBirth();
        breinUser.setDateOfBirth(13, 22, 1966); // this is correct date
        assertTrue(breinUser.getDateOfBirth().isEmpty());

        // set wrong year
        breinUser.resetDateOfBirth();
        breinUser.setDateOfBirth(1, 22, 1700);
        assertTrue(breinUser.getDateOfBirth().isEmpty());
    }

    /**
     * Tests all BreinUser Methods
     */
    @Test
    public void testBreinUserMethods() {

        final BreinUser breinUser = new BreinUser()
                .setFirstName("User")
                .setLastName("Anyhere")
                .setImei("356938035643809")
                .setDateOfBirth(6, 20, 1985)
                .setDeviceId("AAAAAAAAA-BBBB-CCCC-1111-222222220000");

        // assertFalse(breinUser.toString().isEmpty());
    }

    /**
     * Tests all BreinUser Methods
     */
    @Test
    public void testBreinUserWithNoMethods() {

        final BreinUser breinUser = new BreinUser();
        assertFalse(breinUser.toString().isEmpty());
    }

    /**
     * Test of breinActivityType options
     */
    @Test
    public void testBreinActivityTypeSetToPredefinedString() {

        final String breinActivityType = BreinActivityType.CHECKOUT;
        assertTrue(breinActivityType.equals(BreinActivityType.CHECKOUT));
    }

    /**
     * Test of breinActivityType options
     */
    @Test
    public void testBreinActivityTypeSetToAnyString() {

        final String breinActivityType = "whatYouWant";
        assertTrue(breinActivityType.equals("whatYouWant"));
    }

    /**
     * Test of breinCategory options to predefined string
     */
    @Test
    public void testBreinCategoryTypeSetToPredefinedString() {

        final String breinCategoryType = BreinCategoryType.APPAREL;
        assertTrue(breinCategoryType.equals(BreinCategoryType.APPAREL));
    }

    /**
     * Test of breinCategory options to flexible string
     */
    @Test
    public void testBreinCategoryTypeSetToFlexibleString() {

        final String breinCategoryType = "flexibleString";
        assertTrue(breinCategoryType.equals("flexibleString"));
    }

    @Test
    public void testUserAgent() {
        final String userAgent = System.getProperty("http.agent");
        System.out.println("UserAgent is: " + userAgent);
    }

    @Test
    public void testBreinIpInfo() {
        final BreinIpInfo info = BreinIpInfo.getInstance();
        final String externalIp = info.getExternalIp();
        System.out.println("External IP is: " + externalIp);
    }

    @Test
    public void testLocalDateTime() {

        final TimeZone defTimeZone = TimeZone.getDefault();
        System.out.println("Default Timezone is: " + defTimeZone);

        final Calendar c = Calendar.getInstance();
        final Date date = c.getTime();
        final SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ZZZZ (zz)");
        df.setTimeZone(defTimeZone);
        final String strDate = df.format(date);
        System.out.println("Current LocalTimeZone is: " + strDate);
    }
}

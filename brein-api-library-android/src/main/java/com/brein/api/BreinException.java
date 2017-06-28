package com.brein.api;

import android.util.Log;

/**
 * BreinException
 */
public class BreinException extends RuntimeException {

    private static final String TAG = "BreinException";

    /*
     * Error Messages
     */
    public static final String URL_IS_NULL = "URL in request contains null";
    public static final String REQUEST_FAILED = "Failed request!";
    public static final String VALIDATE_ACTIVITY_OR_CONFIG_FAILED = "either activity or config not valid";
    public static final String BREIN_BASE_VALIDATION_FAILED = "activity or lookup object is null";
    public static final String CONFIG_VALIDATION_FAILED = "configuration object is null";
    public static final String REQUEST_BODY_FAILED = "request body is null or wrong";
    public static final String LOOKUP_EXCEPTION = "lookup exception has occurred";
    public static final String ENGINE_NOT_INITIALIZED = "Rest engine not initialized. You have to configure BreinConfig with a valid engine.";
    public static final String USER_NOT_SET = "User not set.";
    public static final String ACTIVITY_TYPE_NOT_SET = "ActivityType not set.";
    public static final String CATEGORY_TYPE_NOT_SET = "CategoryType not set.";

    /*
     * Exception methods...
     */
    public BreinException(final Throwable e) {
        super(e);
        Log.d(TAG, "Exception is: " + e.getMessage());
    }

    public BreinException(final String msg) {
        super(msg);
        Log.d(TAG, "Exception is: " + msg);
    }

    public BreinException(final String msg, final Exception cause) {
        super(msg, cause);
        Log.d(TAG, "Exception is: " + msg + " with cause: " + cause.getMessage());
    }
}

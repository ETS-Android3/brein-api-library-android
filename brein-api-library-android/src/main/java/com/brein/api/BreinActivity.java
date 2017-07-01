package com.brein.api;

import com.brein.domain.BreinConfig;
import com.brein.domain.BreinResult;
import com.brein.domain.BreinUser;
import com.brein.util.BreinMapUtil;
import com.brein.util.BreinUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Sends an activity to the com.brein.engine utilizing the API.
 * The call is done asynchronously as a POST request. It is important
 * that a valid API-key & secret is configured prior before using this class.
 */
public class BreinActivity extends BreinBase<BreinActivity> implements ISecretStrategy, IAsyncExecutable {

    public static final String ACTIVITY_FIELD = "activity";
    public static final String TAGS_FIELD = "tags";

    /**
     * contains the tags
     */
    private Map<String, Object> tagsMap;

    /**
     * contains the fields that are part of the activity map
     */
    private Map<String, Object> activityMap;

    /**
     * returns activity type
     *
     * @return activity type
     */
    public String getActivityType() {
        return getActivityField(ActivityField.TYPE);
    }

    /**
     * Sets activity type
     *
     * @param type to set
     *
     * @return self
     */
    public BreinActivity setActivityType(final String type) {
        ActivityField.TYPE.set(this, type);
        return this;
    }

    /**
     * retrieves brein category. if it is empty or null then
     * the default category (if set) will be used.
     *
     * @return category object
     */
    public String getCategory(final BreinConfig config) {
        final String category = getActivityField(ActivityField.CATEGORY);
        if (BreinUtil.containsValue(category)) {
            return category;
        } else {
            return config.getDefaultCategory();
        }
    }

    /**
     * sets brein category
     *
     * @param category object
     *
     * @return self
     */
    public BreinActivity setCategory(final String category) {
        ActivityField.CATEGORY.set(this, category);
        return this;
    }

    /**
     * retrieves the description
     *
     * @return description
     */
    public String getDescription() {
        return getActivityField(ActivityField.DESCRIPTION);
    }

    /**
     * sets the description
     *
     * @param description string to set as description
     *
     * @return self
     */
    public BreinActivity setDescription(final String description) {
        ActivityField.DESCRIPTION.set(this, description);
        return this;
    }

    /**
     * retrieves the configured activity endpoint (e.g. \activitiy)
     *
     * @return endpoint
     */
    @Override
    public String getEndPoint(final BreinConfig config) {
        return config.getActivityEndpoint();
    }

    // @Override
    public BreinActivity set(final String key, final Object value) {
        if (TAGS_FIELD.equalsIgnoreCase(key)) {
            throw new BreinException("The field '" + TAGS_FIELD + "' cannot be set, " +
                    "use the setTag method to do so.");
        } else if (this.activityMap == null) {
            this.activityMap = new HashMap<>();
        }

        this.activityMap.put(key, value);
        return this;
    }

    /**
     * Sends an activity to the Breinify server.
     *
     * @param breinUser         the user-information
     * @param breinActivityType the type of activity
     * @param breinCategoryType the category (can be null or undefined)
     * @param description       the description for the activity
     */
    public void activity(final BreinUser breinUser,
                         final String breinActivityType,
                         final String breinCategoryType,
                         final String description,
                         final ICallback<BreinResult> callback) {

        /*
         * set the values for further usage
         */
        setUser(breinUser);
        setActivityType(breinActivityType);
        setCategory(breinCategoryType);
        setDescription(description);

        execute(callback);
    }

    /*
    @Override
    public void execute(final ICallback<BreinResult> callback) {
        Breinify.activity(this, callback);
    }
    */

    @Override
    public void execute(ICallback callback) {
        Breinify.activity(this, callback);
    }

    /**
     * initializes the values of this instance
     */
    public void init() {
        setActivityType("");
        setCategory("");
        setDescription("");
        tagsMap = null;
    }


    @Override
    public void prepareRequestData(final BreinConfig config, final Map<String, Object> requestData) {
        final Map<String, Object> activityRequestData = new HashMap<>();

        // add the user-data, if there is any
        if (this.activityMap != null) {
            // loop a Map
            for (Map.Entry<String, Object> entry : this.activityMap.entrySet()) {
                if (BreinUtil.containsValue(entry.getValue())) {
                    activityRequestData.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // we have to set the category again, because it may be set to default
        activityRequestData.put(ActivityField.CATEGORY.getName(), getCategory(config));

        // add tagsMap map if configured
        if (this.tagsMap != null && !this.tagsMap.isEmpty()) {
            activityRequestData.put(TAGS_FIELD, BreinMapUtil.copyMap(this.tagsMap));
        }

        requestData.put(ACTIVITY_FIELD, activityRequestData);
    }

    /**
     * Generates the signature for the request
     *
     * @return full signature
     */
    @Override
    public String createSignature(final BreinConfig config, final Map<String, Object> requestData) {
        final String type = BreinMapUtil.getNestedValue(requestData, ACTIVITY_FIELD, ActivityField.TYPE.getName());
        final String paraType = type == null ? "" : type;

        final long unixTimestamp = BreinMapUtil.getNestedValue(requestData, UNIX_TIMESTAMP_FIELD);

        final String message = String.format("%s%d%d", paraType, unixTimestamp, 1);
        return BreinUtil.generateSignature(message, config.getSecret());
    }

    public BreinActivity setTag(final String key, final Object value) {
        if (this.tagsMap == null) {
            this.tagsMap = new HashMap<>();
        }

        this.tagsMap.put(key, value);
        return this;
    }

    public Map<String, Object> getTagsMap() {
        return this.tagsMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        return this.activityMap == null ? null : (T) this.activityMap.get(key);
    }

    protected <T> T getActivityField(final ActivityField field) {
        return get(field.getName());
    }

    /**
     * This list may not be complete it just contains some values. For a complete list it is recommended to look at the
     * API documentation.
     */
    public enum ActivityField {
        TYPE("type"),
        DESCRIPTION("description"),
        CATEGORY("category");

        final String name;

        ActivityField(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void set(final BreinActivity activity, final Object value) {
            activity.set(getName(), value);
        }
    }

}


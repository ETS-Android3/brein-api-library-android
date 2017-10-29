package com.brein.domain.results.temporaldataparts;

import com.brein.util.BreinUtil;
import com.brein.util.JsonHelper;

import java.util.Map;

public class BreinEventResult {

    private static final String NAME_KEY = "displayName";
    private static final String START_KEY = "startTime";
    private static final String END_KEY = "endTime";
    private static final String CATEGORY_KEY = "category";
    private static final String SIZE_KEY = "sizeEstimated";
    private final String name;
    private final Long start;
    private final Long end;
    private final EventCategory category;
    private final Integer size;

    /**
     * Contains the result of the Event request
     * @param result Map containing the result data
     */
    public BreinEventResult(final Map<String, Object> result) {

        name = JsonHelper.getOr(result, NAME_KEY, null);
        start = JsonHelper.getOrLong(result, START_KEY);
        end = JsonHelper.getOrLong(result, END_KEY);
        final Long innerSize = JsonHelper.getOrLong(result, SIZE_KEY);

        size = innerSize == null || innerSize == -1 ? null : BreinUtil.safeLongToInt(innerSize);

        final String categoryName = JsonHelper.getOr(result, CATEGORY_KEY, "unknown")
                .replaceAll("eventCategory", "")
                .toUpperCase();

        EventCategory tmpCategory = EventCategory.UNKNOWN;
        for (EventCategory eventCategory : EventCategory.values()) {
            final String str = eventCategory.toString();
            if (str.compareToIgnoreCase(categoryName) == 0) {
                tmpCategory = eventCategory;
            }
        }

        category = tmpCategory;
    }

    public String getName() {
        return name;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public EventCategory getCategory() {
        return category;
    }

    public Integer getSize() {
        return size;
    }

    public enum EventCategory {
        CONCERT,
        COMEDY,
        OTHERSHOW,
        POLITICAL,
        SPORTS,
        EDUCATIONAL,
        FITNESS,
        UNKNOWN
    }

}

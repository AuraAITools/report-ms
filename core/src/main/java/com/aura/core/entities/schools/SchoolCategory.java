package com.aura.core.entities.schools;

public enum SchoolCategory {

    PRIMARY("Primary School"),
    SECONDARY("Secondary School"),
    JUNIOR_COLLEGE("Junior College"),
    POLYTECHNIC("Polytechnic"),
    TECHNICAL("Technical Education Institution"),
    INTERNATIONAL("International School"),
    ARTS("Arts Institution"),
    UNIVERSITY("University");

    private final String displayName;

    SchoolCategory(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of the school category.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}

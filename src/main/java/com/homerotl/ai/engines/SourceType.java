package com.homerotl.ai.engines;

/**
 * Different types of content source
 */
public enum SourceType {

    DOGMA(0),
    PERSONAL(1),
    INTERNET(2);

    public int getId() {
        return id;
    }

    private int id;

    SourceType(int id) {
        this.id = id;
    }

    public static SourceType fromId(long id) {
        for (SourceType aType : values()) {
            if (aType.getId()==id) {
                return aType;
            }
        }
        return null;
    }
}

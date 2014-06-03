package com.homerotl.ai.engines;

/**
 * Source of information
 */
public class Source {

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    private SourceType type;
    private String name;
    private long id;

    public long getTimeStamp() {
        return timeStamp;
    }

    private long timeStamp;

    public Source(SourceType type, String name) {
        this.id = -1L;
        this.type = type;
        this.name = name;
        this.timeStamp = System.currentTimeMillis();
    }

    public Source(long id, SourceType type, String name, long timeStamp) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }


}

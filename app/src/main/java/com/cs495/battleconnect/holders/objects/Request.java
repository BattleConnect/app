package com.cs495.battleconnect.holders.objects;

import java.sql.Timestamp;

/**
 * Object used to store the information a user enters when contributing data from the battlefield.
 */
public class Request {
    String tag;
    String condition;
    Timestamp time;
    String comment;

    /**
     * Empty constructor for a request.
     */
    public Request(){}

    /**
     * Constructor for a request.
     * @param tag
     * @param condition
     * @param time
     * @param comment
     */
    public Request(String tag, String condition, Timestamp time, String comment){
        this.tag = tag;
        this.condition = condition;
        this.time = time;
        this.comment = comment;
    }

    /**
     * Gets the ondition value.
     * @return
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition value.
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the comment.
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the timestamp of when the request was created.
     * @return
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Sets the time the request was created.
     * @param time
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * Gets the tag.
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the tag.
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}

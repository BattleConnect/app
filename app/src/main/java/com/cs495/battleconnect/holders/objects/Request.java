package com.cs495.battleconnect.holders.objects;

import java.sql.Timestamp;

public class Request {
    String tag;
    String condition;
    Timestamp time;
    String comment;

    public Request(){}

    public Request(String tag, String condition, Timestamp time, String comment){
        this.tag = tag;
        this.condition = condition;
        this.time = time;
        this.comment = comment;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

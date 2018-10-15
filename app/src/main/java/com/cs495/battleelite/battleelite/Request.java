package com.cs495.battleelite.battleelite;

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
}

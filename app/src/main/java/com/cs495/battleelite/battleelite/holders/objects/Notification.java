package com.cs495.battleelite.battleelite.holders.objects;

public class Notification {
    private String id;
    private String message;
    private String priority;
    private String sender;


    public Notification(String id, String message, String priority, String sender) {
        this.id = id;
        this.message = message;
        this.priority = priority;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

package com.cs495.battleelite.battleelite.responses;

public class NotificationResponse {
    private String id;
    private String sender;
    private String priority;
    private String message;

    public NotificationResponse() {
    }

    public NotificationResponse(String id, String sender, String priority, String message) {
        this.id = id;
        this.sender = sender;
        this.priority = priority;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

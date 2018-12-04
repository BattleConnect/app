package com.cs495.battleelite.battleelite.responses;

/**
 * Object that holds the notification object returned from the firestore database
 */
public class NotificationResponse {
    private String id;
    private String sender;
    private String priority;
    private String message;

    /**
     * Default constructor
     */
    public NotificationResponse() {
    }

    /**
     * Constructor with all fields
     * @param id
     * @param sender
     * @param priority
     * @param message
     */
    public NotificationResponse(String id, String sender, String priority, String message) {
        this.id = id;
        this.sender = sender;
        this.priority = priority;
        this.message = message;
    }

    /**
     * Grabs the notification id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the notification id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Grabs the sender
     * @return
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Grabs the priority
     * @return
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority
     * @param priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Grabs the message
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

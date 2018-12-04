package com.cs495.battleelite.battleelite.holders.objects;

/**
 * Object used to hold notification data from the firestore database
 */
public class Notification {
    private String id;
    private String message;
    private String priority;
    private String sender;

    /**
     * Constructor to create notification object
     * @param id
     * @param message
     * @param priority
     * @param sender
     */
    public Notification(String id, String message, String priority, String sender) {
        this.id = id;
        this.message = message;
        this.priority = priority;
        this.sender = sender;
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
     * Grabs the notification message
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the notification message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Grabs the notification priority
     * @return
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the notification priority
     * @param priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Grabs the notification sender
     * @return
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the notification sender
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
}

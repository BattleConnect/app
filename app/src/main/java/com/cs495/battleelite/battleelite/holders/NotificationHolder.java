package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;

/**
 * Holds all the visual elements of a notification item in the notification recycler view
 */
public class NotificationHolder extends RecyclerView.ViewHolder {
    public TextView id;
    public TextView message;
    public TextView sender;
    public TextView priority;
    public LinearLayout parentLayout;

    /**
     * Constructor for the notification item
     * @param itemView
     */
    public NotificationHolder(View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.notification_id);
        message = itemView.findViewById(R.id.notification_message);
        sender = itemView.findViewById(R.id.notification_sender);
        priority = itemView.findViewById(R.id.notification_priority);

        parentLayout = itemView.findViewById(R.id.parentLayout);
    }

    /**
     * Set the id textview
     * @param id
     */
    public void setId(TextView id) {
        this.id = id;
    }

    /**
     * Set the message textview
     * @param message
     */
    public void setMessage(TextView message) {
        this.message = message;
    }

    /**
     * Set the sender textview
     * @param sender
     */
    public void setSender(TextView sender) {
        this.sender = sender;
    }

    /**
     * Set the priority textview
     * @param priority
     */
    public void setPriority(TextView priority) {
        this.priority = priority;
    }
}

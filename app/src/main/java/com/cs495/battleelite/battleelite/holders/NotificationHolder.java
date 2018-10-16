package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;

public class NotificationHolder extends RecyclerView.ViewHolder {
    public TextView id;
    public TextView message;
    public TextView sender;
    public TextView priority;
    public LinearLayout parentLayout;

    public NotificationHolder(View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.notification_id);
        message = itemView.findViewById(R.id.notification_message);
        sender = itemView.findViewById(R.id.notification_sender);
        priority = itemView.findViewById(R.id.notification_priority);

        parentLayout = itemView.findViewById(R.id.parentLayout);
    }

    public void setId(TextView id) {
        this.id = id;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public void setSender(TextView sender) {
        this.sender = sender;
    }

    public void setPriority(TextView priority) {
        this.priority = priority;
    }
}

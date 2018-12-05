package com.cs495.battleelite.battleelite.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs495.battleelite.battleelite.R;


public class ForceHolder extends RecyclerView.ViewHolder  {

        public TextView Date_Time;
        public TextView Lat;
        public TextView Long;
        public TextView Force_ID;
        public ImageView Force_Type;
        public TextView Force_Status;
        public TextView name;
        public LinearLayout parentLayout;

        public ForceHolder(View itemView) {
            super(itemView);

            Date_Time = itemView.findViewById(R.id.date_time);
            Lat = itemView.findViewById(R.id.latitude);
            Long = itemView.findViewById(R.id.longitude);
            Force_ID = itemView.findViewById(R.id.id);
            Force_Type = itemView.findViewById(R.id.force_type_icon);
            Force_Status = itemView.findViewById(R.id.status);
            name =  itemView.findViewById(R.id.name);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

        public void setDate_time(TextView date_time) {
            this.Date_Time = date_time;
        }

        public void setLatitude(TextView latitude) {
            this.Lat = latitude;
        }

        public void setLongitude(TextView longitude) {
            this.Long = longitude;
        }

        public void setForce_ID(TextView id) {
            this.Force_ID = id;
        }

        public void setForce_Type(ImageView type) {
           // this.Force_Type = type;
        }

        public void setForce_Status(TextView status) {
            this.Force_Status = status;
        }

        public void setName(TextView name) {
            this.name = name;
        }

}



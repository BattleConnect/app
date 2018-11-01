package com.cs495.battleelite.battleelite.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cs495.battleelite.battleelite.FilterDialogFragment;
import com.cs495.battleelite.battleelite.R;

public class NotificationFilterFragment extends DialogFragment {
    NotificationFilterFragment.NotificationFilterFragmentListener mListener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View v = inflater.inflate(R.layout.dialog_filter, null);
            builder.setView(v);
            builder.setTitle(R.string.filterButton);
            final Spinner notificationTypes = (Spinner) v.findViewById(R.id.sensorFilterSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.notificationTypeList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            notificationTypes.setAdapter(adapter);
            builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.getSelectedNotificationPriorityFilter(notificationTypes.getSelectedItem().toString());

                }
            });
            builder.setNegativeButton(R.string.filterNegative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        public interface NotificationFilterFragmentListener{
            public void getSelectedNotificationPriorityFilter(String type);

        }

        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try{
                mListener = (NotificationFilterFragment.NotificationFilterFragmentListener) context;
            } catch (ClassCastException e){
                throw new ClassCastException(getActivity().toString() + " must implement FilterDialogFragmentListener");
            }
        }
    }

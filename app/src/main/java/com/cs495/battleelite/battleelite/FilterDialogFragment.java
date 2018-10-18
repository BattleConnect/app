package com.cs495.battleelite.battleelite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

public class FilterDialogFragment extends DialogFragment {
    FilterDialogFragmentListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(v);
        builder.setTitle(R.string.filterButton)
                .setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Spinner sensorTypes = (Spinner) v.findViewById(R.id.sensorFilterSpinner);
                        mListener.getSelectedSensorTypeFilter(sensorTypes.getSelectedItem().toString());

                    }
                })
                .setNegativeButton(R.string.filterNegative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface FilterDialogFragmentListener{
        public void getSelectedSensorTypeFilter(String type);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (FilterDialogFragmentListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement FilterDialogFragmentListener");
        }
    }
}

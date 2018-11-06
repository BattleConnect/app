package com.cs495.battleelite.battleelite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {
    FilterDialogFragmentListener mListener;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ExpListViewAdapterWithCheckbox listAdapter;
        ExpandableListView expListView;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(v);
        builder.setTitle(R.string.filterButton);


        final Spinner sensorTypes = (Spinner) v.findViewById(R.id.sensorFilterSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sensorTypeList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorTypes.setAdapter(adapter);


        loadListData();
        expListView = (ExpandableListView) v.findViewById(R.id.expandableFilter);
        listAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


                builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.getSelectedSensorTypeFilter(sensorTypes.getSelectedItem().toString());

                    }
                });
                builder.setNegativeButton(R.string.filterNegative, new DialogInterface.OnClickListener() {
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

    public void loadListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Sensor Type");


        List<String> sensorType = new ArrayList();
        sensorType.add(getResources().getString(R.string.asset));
        sensorType.add(getResources().getString(R.string.heartbeat));
        sensorType.add(getResources().getString(R.string.vibration));
        sensorType.add(getResources().getString(R.string.moisture));
        sensorType.add(getResources().getString(R.string.temperature));



        listDataChild.put(listDataHeader.get(0), sensorType);

    }
}

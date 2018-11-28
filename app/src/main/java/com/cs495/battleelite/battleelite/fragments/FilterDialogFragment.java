package com.cs495.battleelite.battleelite.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import com.cs495.battleelite.battleelite.ExpListViewAdapterWithCheckbox;
import com.cs495.battleelite.battleelite.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    FilterDialogFragmentListener mListener;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    boolean[] checkedStates;

    public static FilterDialogFragment newInstance(boolean[] checkedStatesFromActivity){
         FilterDialogFragment f = new FilterDialogFragment();
         Bundle args = new Bundle();
         args.putBooleanArray("checkedStatesFromActivity", checkedStatesFromActivity);
         f.setArguments(args);

         return f;
    }

    public boolean[] getCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("checkedStatesFromActivity");
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        final ExpListViewAdapterWithCheckbox listAdapter;
        final ExpandableListView expListView;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(v);
        builder.setTitle(R.string.filterButton);


        


        loadListData();
        expListView = (ExpandableListView) v.findViewById(R.id.expandableFilter);
        checkedStates = getCheckedStatesFromActivity();
        listAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), listDataHeader, listDataChild, checkedStates);
        expListView.setAdapter(listAdapter);


                builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.getMultipleSelectedSensorFilters(listAdapter.getSelectedItems(0));
                        mListener.getSelectedFilterIndicesBoolean(listAdapter.getSelectedFilterIndicesBoolean(0));

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
        public void getMultipleSelectedSensorFilters(List<String> filters);
        public void getSelectedFilterIndicesBoolean(boolean[] indices);

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

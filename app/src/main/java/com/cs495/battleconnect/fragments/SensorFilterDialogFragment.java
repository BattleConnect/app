package com.cs495.battleconnect.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import com.cs495.battleconnect.UI.ExpListViewAdapterWithCheckbox;
import com.cs495.battleconnect.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the dialog that shows the filter options for filtering sensor data when exploring sensor data.
 */
public class SensorFilterDialogFragment extends DialogFragment {

    FilterDialogFragmentListener mListener;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    boolean[] checkedStates;
    boolean[] otherCheckedStates;

    public static SensorFilterDialogFragment newInstance(boolean[] checkedStatesFromActivity, boolean[] otherCheckedStatesFromActivity){
         SensorFilterDialogFragment f = new SensorFilterDialogFragment();
         Bundle args = new Bundle();
         args.putBooleanArray("checkedStatesFromActivity", checkedStatesFromActivity);
         args.putBooleanArray("otherCheckedStatesFromActivity", otherCheckedStatesFromActivity);
         f.setArguments(args);

         return f;
    }

    /**
     * function gets the filters that are already selected by passing them back and forth between activity and fragment
     * @return checked states from the activity
     */

    public boolean[] getCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("checkedStatesFromActivity");
    }

    /**
     * same as other getChecked function, but for the "other tab"
     * @return checked filters under the "other" tab
     */
    public boolean[] getOtherCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("otherCheckedStatesFromActivity");
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
        otherCheckedStates = getOtherCheckedStatesFromActivity();
        listAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), listDataHeader, listDataChild, checkedStates, otherCheckedStates);
        expListView.setAdapter(listAdapter);


                builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        List<String> filters = new ArrayList();
                        filters.addAll(listAdapter.getSelectedItems(0));
                        filters.addAll(listAdapter.getSelectedItems(1));
                        mListener.getMultipleSelectedSensorFilters(filters);
                        mListener.getSelectedFilterIndicesBoolean(listAdapter.getSelectedFilterIndicesBoolean(0));
                        mListener.getOtherSelectedFilterIndicesBoolean(listAdapter.getSelectedFilterIndicesBoolean(1));

                    }
                });
                builder.setNegativeButton(R.string.filterNegative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
        this interface allows the filter values and their respective indices to be passed back to the activity
     */

    public interface FilterDialogFragmentListener{
        void getMultipleSelectedSensorFilters(List<String> filters);
        void getSelectedFilterIndicesBoolean(boolean[] indices);
        void getOtherSelectedFilterIndicesBoolean(boolean[] indices);
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

    /**
     * sets up the filter list options
     */

    public void loadListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Sensor Type");
        listDataHeader.add("Other");

        List<String> sensorType = new ArrayList();
        sensorType.add(getResources().getString(R.string.asset));
        sensorType.add(getResources().getString(R.string.heartbeat));
        sensorType.add(getResources().getString(R.string.vibration));
        sensorType.add(getResources().getString(R.string.moisture));
        sensorType.add(getResources().getString(R.string.temperature));

        List<String> otherFilters = new ArrayList();
        otherFilters.add(getResources().getString(R.string.heartbeat_zero));
        otherFilters.add(getResources().getString(R.string.tripped_vibration));
        otherFilters.add(getResources().getString(R.string.dead_battery));


        listDataChild.put(listDataHeader.get(0), sensorType);
        listDataChild.put(listDataHeader.get(1), otherFilters);

    }
}

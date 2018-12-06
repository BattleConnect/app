package com.cs495.battleconnect.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.cs495.battleconnect.UI.ExpListViewAdapterWithCheckbox;
import com.cs495.battleconnect.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This dialog shows all the filter options for the map.
 */
public class MapFilterFragment extends DialogFragment {

    MapFilterFragment.MapFilterFragmentListener mListener;

    ArrayList<String> toggleDataHeader;
    ArrayList<String> sensorDataHeader;
    ArrayList<String> forceDataHeader;
    ArrayList<String> otherDataHeader;

    HashMap<String, List<String>> toggleDataChild;
    HashMap<String, List<String>> sensorDataChild;
    HashMap<String, List<String>> forceDataChild;
    HashMap<String, List<String>> otherDataChild;

    boolean[] toggleCheckedStates;
    boolean[] sensorCheckedStates;
    boolean[] forceCheckedStates;
    boolean[] otherCheckedStates;

    /**
     * Constructor for this dialog. Passes in all of the filters that were checked and applied previously.
     * @param toggleCheckedStates Toggle filters checked and applied previously.
     * @param sensorCheckedStates Sensor types checked and applied previously.
     * @param forceCheckedStates Force types checked and applied previously.
     * @param otherCheckedStates Other filters checked and applied previously.
     * @return
     */
    public static MapFilterFragment newInstance(boolean[] toggleCheckedStates, boolean[] sensorCheckedStates, boolean[] forceCheckedStates, boolean[] otherCheckedStates) {
        MapFilterFragment f = new MapFilterFragment();
        Bundle args = new Bundle();
        args.putBooleanArray("toggleCheckedStates", toggleCheckedStates);
        args.putBooleanArray("sensorCheckedStates", sensorCheckedStates);
        args.putBooleanArray("forceCheckedStates", forceCheckedStates);
        args.putBooleanArray("otherCheckedStates", otherCheckedStates);
        f.setArguments(args);

        return f;
    }

    /**
     * Get the toggle filters that were checked and applied previously from the MapActivity.
     * @return
     */
    public boolean[] getToggleCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("toggleCheckedStates");
    }

    /**
     * Get the sensor type filters that were checked and applied previously from the MapActivity.
     * @return
     */
    public boolean[] getSensorCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("sensorCheckedStates");
    }

    /**
     * Get the force type filters that were checked and applied previously from the MapActivity.
     * @return
     */
    public boolean[] getForceCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("forceCheckedStates");
    }

    /**
     * Get the other filters that were checked and applied previously from the MapActivity.
     * @return
     */
    public boolean[] getOtherCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("otherCheckedStates");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.map_filter_dialog, null);
        builder.setView(v);
        builder.setTitle(R.string.filterButton);

        ExpandableListView toggleListView = v.findViewById(R.id.toggleFilter);
        ExpandableListView sensorListView = v.findViewById(R.id.sensorFilter);
        ExpandableListView forceListView = v.findViewById(R.id.forceFilter);
        ExpandableListView otherListView = v.findViewById(R.id.otherFilter);

        loadToggleData();
        loadSensorData();
        loadForceData();
        loadOtherData();

        toggleCheckedStates = getToggleCheckedStatesFromActivity();
        sensorCheckedStates = getSensorCheckedStatesFromActivity();
        forceCheckedStates = getForceCheckedStatesFromActivity();
        otherCheckedStates = getOtherCheckedStatesFromActivity();

        final ExpListViewAdapterWithCheckbox toggleAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), toggleDataHeader, toggleDataChild, toggleCheckedStates, null);
        final ExpListViewAdapterWithCheckbox sensorAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), sensorDataHeader, sensorDataChild, sensorCheckedStates, null);
        final ExpListViewAdapterWithCheckbox forceAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), forceDataHeader, forceDataChild, forceCheckedStates, null);
        final ExpListViewAdapterWithCheckbox otherAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), otherDataHeader, otherDataChild, otherCheckedStates, null);

        toggleListView.setAdapter(toggleAdapter);
        sensorListView.setAdapter(sensorAdapter);
        forceListView.setAdapter(forceAdapter);
        otherListView.setAdapter(otherAdapter);

        builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(toggleAdapter != null && toggleAdapter.getGroupCount() != 0 && toggleAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedToggleFilter(toggleAdapter.getSelectedItems(0));
                    mListener.getSelectedToggleFilterIndicesBoolean(toggleAdapter.getSelectedFilterIndicesBoolean(0));
                }

                if(sensorAdapter != null && sensorAdapter.getGroupCount() != 0 && sensorAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedSensorTypeFilter(sensorAdapter.getSelectedItems(0));
                    mListener.getSelectedSensorFilterIndicesBoolean(sensorAdapter.getSelectedFilterIndicesBoolean(0));
                }

                if(forceAdapter != null && forceAdapter.getGroupCount() != 0 && forceAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedForceTypeFilter(forceAdapter.getSelectedItems(0));
                    mListener.getSelectedForceFilterIndicesBoolean(forceAdapter.getSelectedFilterIndicesBoolean(0));
                }

                if(otherAdapter != null && otherAdapter.getGroupCount() != 0 && otherAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedOtherFilter(otherAdapter.getSelectedItems(0));
                    mListener.getSelectedOtherFilterIndicesBoolean(otherAdapter.getSelectedFilterIndicesBoolean(0));
                }
            }
        });
        builder.setNegativeButton(R.string.filterNegative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface MapFilterFragmentListener{
        void getSelectedToggleFilter(List<String> filters);
        void getSelectedSensorTypeFilter(List<String> filters);
        void getSelectedForceTypeFilter(List<String> filters);
        void getSelectedOtherFilter(List<String> filters);
        void getSelectedToggleFilterIndicesBoolean(boolean[] indices);
        void getSelectedSensorFilterIndicesBoolean(boolean[] indices);
        void getSelectedForceFilterIndicesBoolean(boolean[] indices);
        void getSelectedOtherFilterIndicesBoolean(boolean[] indices);
    }

    /**
     * Loads the toggle filter options.
     */
    private void loadToggleData() {
        toggleDataHeader = new ArrayList<String>();
        toggleDataChild = new HashMap<String, List<String>>();

        toggleDataHeader.add("Toggle Forces and Sensors");

        List<String> toggles = new ArrayList<>();
        toggles.add(getResources().getString(R.string.forces));
        toggles.add(getResources().getString(R.string.sensors));

        toggleDataChild.put(toggleDataHeader.get(0), toggles);
    }

    /**
     * Loads the other filter options.
     */
    private void loadOtherData() {
        otherDataHeader = new ArrayList<String>();
        otherDataChild = new HashMap<String, List<String>>();

        otherDataHeader.add("Other");

        List<String> sensorType = new ArrayList<>();
        sensorType.add(getResources().getString(R.string.heartbeat_zero));
        sensorType.add(getResources().getString(R.string.tripped_vibration));
        sensorType.add("Health=Service");
        sensorType.add("Health=EOL");
        sensorType.add(getResources().getString(R.string.dead_battery));

        otherDataChild.put(otherDataHeader.get(0), sensorType);
    }

    /**
     * Loads the sensor type filter options.
     */
    private void loadSensorData() {
        sensorDataHeader = new ArrayList<String>();
        sensorDataChild = new HashMap<String, List<String>>();

        sensorDataHeader.add("Sensor Type");

        List<String> sensorType = new ArrayList<>();
        sensorType.add(getResources().getString(R.string.asset));
        sensorType.add(getResources().getString(R.string.heartbeat));
        sensorType.add(getResources().getString(R.string.vibration));
        sensorType.add(getResources().getString(R.string.moisture));
        sensorType.add(getResources().getString(R.string.temperature));

        sensorDataChild.put(sensorDataHeader.get(0), sensorType);
    }

    /**
     * Loads the force type filter options.
     */
    private void loadForceData() {
        forceDataHeader = new ArrayList<String>();
        forceDataChild = new HashMap<String, List<String>>();

        forceDataHeader.add("Force Type");

        List<String> forceType = new ArrayList<>();
        forceType.add(getResources().getString(R.string.company));
        forceType.add(getResources().getString(R.string.platoon));
        forceType.add(getResources().getString(R.string.squad));
        forceType.add(getResources().getString(R.string.enemy));
        forceType.add(getResources().getString(R.string.target));

        forceDataChild.put(forceDataHeader.get(0), forceType);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (MapFilterFragment.MapFilterFragmentListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement MapFilterFragmentListener");
        }
    }
}

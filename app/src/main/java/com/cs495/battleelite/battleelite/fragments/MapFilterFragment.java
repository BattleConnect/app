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
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.cs495.battleelite.battleelite.ExpListViewAdapterWithCheckbox;
import com.cs495.battleelite.battleelite.ExpandableListAdapter;
import com.cs495.battleelite.battleelite.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFilterFragment extends DialogFragment {
    MapFilterFragment.MapFilterFragmentListener mListener;
    ArrayList<String> sensorDataHeader;
    ArrayList<String> forceDataHeader;
    HashMap<String, List<String>> sensorDataChild;
    HashMap<String, List<String>> forceDataChild;
    boolean[] sensorCheckedStates;
    boolean[] forceCheckedStates;

    public static MapFilterFragment newInstance(boolean[] sensorCheckedStates, boolean[] forceCheckedStates) {
        MapFilterFragment f = new MapFilterFragment();
        Bundle args = new Bundle();
        args.putBooleanArray("sensorCheckedStates", sensorCheckedStates);
        args.putBooleanArray("forceCheckedStates", forceCheckedStates);
        f.setArguments(args);

        return f;
    }

    public boolean[] getSensorCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("sensorCheckedStates");
    }

    public boolean[] getForceCheckedStatesFromActivity(){
        return getArguments().getBooleanArray("forceCheckedStates");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.map_filter_dialog, null);
        builder.setView(v);
        builder.setTitle(R.string.filterButton);

        ExpandableListView sensorListView = (ExpandableListView) v.findViewById(R.id.sensorFilter);
        ExpandableListView forceListView = (ExpandableListView) v.findViewById(R.id.forceFilter);

        loadSensorData();
        loadForceData();

        sensorCheckedStates = getSensorCheckedStatesFromActivity();
        forceCheckedStates = getForceCheckedStatesFromActivity();

        final ExpListViewAdapterWithCheckbox sensorAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), sensorDataHeader, sensorDataChild, sensorCheckedStates);
        final ExpListViewAdapterWithCheckbox forceAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), forceDataHeader, forceDataChild, forceCheckedStates);

        sensorListView.setAdapter(sensorAdapter);
        forceListView.setAdapter(forceAdapter);

        builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(sensorAdapter != null && sensorAdapter.getGroupCount() != 0 && sensorAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedSensorTypeFilter(sensorAdapter.getSelectedItems(0));
                }

                if(forceAdapter != null && forceAdapter.getGroupCount() != 0 && forceAdapter.getSelectedItems(0) != null) {
                    mListener.getSelectedForceTypeFilter(forceAdapter.getSelectedItems(0));
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
        public void getSelectedSensorTypeFilter(List<String> filters);
        public void getSelectedForceTypeFilter(List<String> filters);
        public void getSelectedSensorFilterIndicesBoolean(boolean[] indices);
        public void getSelectedForceFilterIndicesBoolean(boolean[] indices);
    }

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

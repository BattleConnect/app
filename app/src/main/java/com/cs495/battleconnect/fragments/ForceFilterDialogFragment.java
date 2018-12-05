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

public class ForceFilterDialogFragment extends DialogFragment {

    FilterDialogFragmentListener mListener;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    boolean[] checkedStates;

    public static ForceFilterDialogFragment newInstance(boolean[] checkedStatesFromActivity){
        ForceFilterDialogFragment f = new ForceFilterDialogFragment();
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
        listAdapter = new ExpListViewAdapterWithCheckbox(this.getActivity(), listDataHeader, listDataChild, checkedStates, null);
        expListView.setAdapter(listAdapter);


        builder.setPositiveButton(R.string.filterPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                List<String> filters = new ArrayList();
                filters.addAll(listAdapter.getSelectedItems(0));
                mListener.getMultipleSelectedSensorFilters(filters);
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

    public interface FilterDialogFragmentListener {
        void getMultipleSelectedSensorFilters(List<String> filters);
        void getSelectedFilterIndicesBoolean(boolean[] indices);
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
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Force Type");

        List<String> forceType = new ArrayList();
        forceType.add(getResources().getString(R.string.company));
        forceType.add(getResources().getString(R.string.platoon));
        forceType.add(getResources().getString(R.string.squad));
        forceType.add(getResources().getString(R.string.target));
        forceType.add(getResources().getString(R.string.enemy));

        listDataChild.put(listDataHeader.get(0), forceType);
    }
}
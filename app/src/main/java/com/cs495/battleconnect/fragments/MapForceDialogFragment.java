package com.cs495.battleconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.holders.objects.ForceData;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class shows the data associated with a force when that force's marker is clicked on the map.
 */
public class MapForceDialogFragment extends DialogFragment {
    private static final String FORCE_DATA = "param1";
    private ForceData mForceData;

    /**
     * The constructor for this class.
     * @param forceData The data associated with the force whose data we want to display.
     * @return
     */
    public static MapForceDialogFragment newInstance(ForceData forceData) {
        MapForceDialogFragment fragment = new MapForceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FORCE_DATA, forceData);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mForceData = (ForceData) getArguments().getSerializable(FORCE_DATA);
        return inflater.inflate(R.layout.fragment_map_force_dialog, container, false);
    }


    public MapForceDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceStat) {
        //After the view is created, update the information on the screen to reflect the sensor's data.
        updateForceInfo();

        //Configure the listener on the close button.
        final Button button = getView().findViewById(R.id.button_close);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * Updates the information shown about the force on the screen.
     */
    void updateForceInfo() {
        if (getView() == null) {
            return;
        }

        ((TextView) getView().findViewById(R.id.name)).setText(String.valueOf(mForceData.getName()));

        ((TextView) getView().findViewById(R.id.id)).setText(String.valueOf(mForceData.getID()));

        ((ImageView) getView().findViewById(R.id.force_type_icon)).setImageResource(getForceIcon());

        ((TextView) getView().findViewById(R.id.status)).setText(String.valueOf(mForceData.getStatus()));

        String timestamp = new SimpleDateFormat("MMM dd HH:mm:ss").format(new Date(mForceData.getDate_Time()));

        ((TextView) getView().findViewById(R.id.date_time)).setText(timestamp);

        ((TextView) getView().findViewById(R.id.latitude)).setText(String.valueOf(mForceData.getLat()));

        ((TextView) getView().findViewById(R.id.longitude)).setText(String.valueOf(mForceData.getLong()));
    }


    /**
     * Gets the icon associated with a sensor based on the sensor's type.
     * @return
     */
    public int getForceIcon() {
        String forceType = mForceData.getType();

        if (forceType.equals("Company HQ"))
                return R.drawable.company_hq;
        else if (forceType.equals("Squad"))
            return R.drawable.squad;
        else if (forceType.equals("Platoon"))
            return R.drawable.platoon;
        else if (forceType.equals("Preplanned Target"))
            return R.drawable.target;
        else if (forceType.equals("Enemy Unit"))
            return R.drawable.enemy_unit;

        return 0;
    }
}
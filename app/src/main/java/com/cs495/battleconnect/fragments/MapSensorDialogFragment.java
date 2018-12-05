package com.cs495.battleconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.holders.objects.SensorData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapSensorDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapSensorDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapSensorDialogFragment extends DialogFragment {
    private static final String SENSOR_DATA = "param1";
    private SensorData mSensorData;

    public static MapSensorDialogFragment newInstance(SensorData sensorData) {
        MapSensorDialogFragment fragment = new MapSensorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SENSOR_DATA, sensorData);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mSensorData = (SensorData) getArguments().getSerializable(SENSOR_DATA);
        return inflater.inflate(R.layout.fragment_map_sensor_dialog, container, false);
    }

        //private OnFragmentInteractionListener mListener;

    public MapSensorDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceStat) {
        updateSensorInfo();

        final Button button = getView().findViewById(R.id.button_close);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
//
//        final Button button = getView().findViewById(R.id.see_history_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putLong("SENSOR_ID", mSensorData.getSensor_ID());
//                SensorHistoryFragment fragment = new SensorHistoryFragment();
//                fragment.setArguments(bundle);
//
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.frameLayout, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//                dismiss();
//            }
//        });
    }


    void updateSensorInfo() {
        if (getView() == null) {
            return;
        }

        ((TextView) getView().findViewById(R.id.id)).setText(String.valueOf(mSensorData.getSensor_ID()));

        ((ImageView) getView().findViewById(R.id.sensor_type_icon)).setImageResource(getSensorIcon());

        ((TextView) getView().findViewById(R.id.value)).setText(String.valueOf(mSensorData.getSensor_Val()));

        String timestamp = new SimpleDateFormat("MMM dd HH:mm:ss").format(new Date(mSensorData.getDate_Time()));

        ((TextView) getView().findViewById(R.id.date_time)).setText(timestamp);

        ((TextView) getView().findViewById(R.id.latitude)).setText(String.valueOf(mSensorData.getLat()));

        ((TextView) getView().findViewById(R.id.longitude)).setText(String.valueOf(mSensorData.getLong()));

        TextView sensorHealthTextView = getView().findViewById(R.id.health);
        sensorHealthTextView.setText(mSensorData.getSensorHealth());

        LinearLayout healthWrapper = getView().findViewById(R.id.health_wrapper);
        if (mSensorData.getSensorHealth().equals("Good"))
            healthWrapper.setBackgroundResource(R.color.good_health);
        else
            healthWrapper.setBackgroundResource(R.color.bad_health);

        TextView batteryTextView = getView().findViewById(R.id.battery);
        batteryTextView.setText(mSensorData.getBattery() + "%");

        LinearLayout batteryWrapper = getView().findViewById(R.id.battery_wrapper);

        ImageView batteryIcon = getView().findViewById(R.id.bat_icon);
        if (mSensorData.getBattery() <= 5) {
            batteryWrapper.setBackgroundResource(R.color.low_battery);
            batteryIcon.setBackgroundColor(getResources().getColor(R.color.low_battery));
        } else if (mSensorData.getBattery() <= 20) {
            batteryWrapper.setBackgroundResource(R.color.med_battery);
            batteryIcon.setBackgroundColor(getResources().getColor(R.color.med_battery));
        } else {
            batteryWrapper.setBackgroundResource(R.color.high_battery);
            batteryIcon.setBackgroundColor(getResources().getColor(R.color.high_battery));
        }
    }


    public int getSensorIcon() {
        String sensorType = mSensorData.getSensor_Type();

        if (sensorType.equals("HeartRate")) {
            if (mSensorData.getSensor_Val() == 0)
                return R.drawable.dead_heartrate;
            else
                return R.drawable.pointer_heart;
        }
        else if (sensorType.equals("Moisture"))
            return R.drawable.water_drop;
        else if (sensorType.equals("Vibration"))
            return R.drawable.vibration1;
        else if (sensorType.equals("Asset"))
            return R.drawable.diamond;
        else if (sensorType.equals("Temperature"))
            return R.drawable.thermometer;

        return 0;
    }
}
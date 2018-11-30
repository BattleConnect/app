package com.cs495.battleelite.battleelite.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cs495.battleelite.battleelite.R;
import com.cs495.battleelite.battleelite.holders.objects.SensorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SENSOR_ID = "param1";

    // TODO: Rename and change types of parameters
    private long mSensorId;

    //private OnFragmentInteractionListener mListener;

    public SensorHistoryFragment() {
        // Required empty public constructor
    }

    private static final String TAG = SensorHistoryFragment.class.getName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<SensorData> sensors = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SensorHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static SensorHistoryFragment newInstance(long param1) {
//        SensorHistoryFragment fragment = new SensorHistoryFragment();
//        Bundle args = new Bundle();
//        args.putLong(SENSOR_ID, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSensorId = getArguments().getLong("SENSOR_ID");
            getSensorData(mSensorId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor_history, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceStat) {
//    }

    void getSensorData(final long sensorId) {
        System.out.println("getting sensor data");
        db.collection("sensors")
                .whereEqualTo("Sensor_ID", sensorId)
                .orderBy("Date_Time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                SensorData sensorData = document.toObject(SensorData.class);
                                sensors.add(sensorData);
                            }
                            updateSensorInfo();
                            updateGraph();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void updateSensorInfo() {

        SensorData sensorData = sensors.get(sensors.size()-1);

        ((TextView) getView().findViewById(R.id.id)).setText(String.valueOf(sensorData.getSensor_ID()));

        ((TextView) getView().findViewById(R.id.type)).setText(sensorData.getSensor_Type());

        ((TextView) getView().findViewById(R.id.value)).setText(String.valueOf(sensorData.getSensor_Val()));

        String timestamp = new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date(sensorData.getDate_Time()));
        ((TextView) getView().findViewById(R.id.date_time)).setText(timestamp);

        ((TextView) getView().findViewById(R.id.latitude)).setText(String.valueOf(sensorData.getLat()));

        ((TextView) getView().findViewById(R.id.longitude)).setText(String.valueOf(sensorData.getLong()));

        ((TextView) getView().findViewById(R.id.health)).setText(sensorData.getSensorHealth());

        ((TextView) getView().findViewById(R.id.battery)).setText(sensorData.getBattery() + "%");
    }

    void updateGraph() {
        GraphView graph = getView().findViewById(R.id.graph);
        ArrayList<DataPoint> dataList = data();

        DataPoint[] dataArray = dataList.toArray(new DataPoint[dataList.size()]);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataArray);
        graph.addSeries(series);

        // set date label formatter
        String pattern = "MM/dd HH:mm";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) (value));
                    return (simpleDateFormat.format(d));
                    //return super.formatLabel(value, isValueX);
                }
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//
//        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(dataList.get(0).getX());
//        graph.getViewport().setMaxX(dataList.get(dataList.size() - 1).getX());
//        graph.getViewport().setXAxisBoundsManual(true);
//

//
//
//        graph.getGridLabelRenderer().setNumVerticalLabels(5); // only 4 because of the space
//        graph.getViewport().setBackgroundColor(2);
    }

    public ArrayList<DataPoint> data(){
        ArrayList<DataPoint> values = new ArrayList<>();
        for (SensorData sensorData : sensors) {
            DataPoint dataPoint = new DataPoint(sensorData.getDate_Time(), sensorData.getSensor_Val());
            values.add(dataPoint);
        }
        return values;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}

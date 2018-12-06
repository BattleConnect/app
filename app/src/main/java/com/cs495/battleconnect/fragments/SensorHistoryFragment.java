package com.cs495.battleconnect.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.holders.objects.SensorData;
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
 * This fragment shows the history of a sensor.
 * The history of a sensor consists of a graph showing the sensor's value over time.
 */
public class SensorHistoryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SENSOR_ID = "param1";

    private long mSensorId;

    public SensorHistoryFragment() {
        // Required empty public constructor
    }

    private static final String TAG = SensorHistoryFragment.class.getName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<SensorData> sensorDataList = new ArrayList<>();

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


    /**
     * Gets all of the data associated with a particular sensor from Firebase.
     * @param sensorId
     */
    void getSensorData(final long sensorId) {
        System.out.println("getting sensor data");
        db.collection("sensorDataList")
                .whereEqualTo("Sensor_ID", sensorId)
                .orderBy("Date_Time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                SensorData sensorData = document.toObject(SensorData.class);
                                sensorDataList.add(sensorData);
                            }
                            updateSensorInfo();
                            updateGraph();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Updates the information shown about the sensor on the screen.
     */
    void updateSensorInfo() {
        //The view may be null if the user clicked the back button immediately after opening the sensor history fragment.
        if (getView() == null) {
            Log.d("SensorHistory", "View is null.");
            return;
        }

        SensorData sensorData = sensorDataList.get(sensorDataList.size()-1);

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

    /**
     * Updates the data in the graph shown on the screen.
     * The graph shows the values of a sensor over time. Y-axis = sensor values. X-axis = timestamps.
     */
    void updateGraph() {
        //The view may be null if the user clicked the back button immediately after opening the sensor history fragment.
        if (getView() == null) {
            Log.d("SensorHistory", "View is null.");
            return;
        }

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
                }
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        //There isn't enough room on the screen to show more than three x-axis labels.
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
    }

    /**
     * Converts data about the sensor into data points for the graph.
     * @return
     */
    public ArrayList<DataPoint> data(){
        ArrayList<DataPoint> values = new ArrayList<>();
        for (SensorData sensorData : sensorDataList) {
            DataPoint dataPoint = new DataPoint(sensorData.getDate_Time(), sensorData.getSensor_Val());
            values.add(dataPoint);
        }
        return values;
    }
}

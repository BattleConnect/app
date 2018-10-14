package com.cs495.battleelite.battleelite;

import com.google.firebase.firestore.*;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nullable;

public class SensorDataManager {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Map<Long, Map<String, Object>> sensors; //long = Sensor_ID, Object = most recent sensor data entry

    SensorDataManager() {
        sensors = new HashMap<Long, Map<String, Object>>();
        db.collection("sensors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Long sensorID = (Long) dc.getDocument().get("Sensor_ID");
                    switch (dc.getType()) {
                        case ADDED:
                            if (sensors.containsKey(sensorID)) {
                                Map<String, Object> oldData = sensors.get(sensorID);
                                Map<String, Object> newData = dc.getDocument().getData();
                                Double oldDateTime = (Double) oldData.get("Date_Time");
                                Double newDateTime = (Double) newData.get("Date_Time");
                                if (newDateTime > oldDateTime)
                                    sensors.put(sensorID, newData);
                            }
                            else
                                sensors.put(sensorID, dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            if (sensors.containsKey(sensorID)) {
                                Map<String, Object> oldData = sensors.get(sensorID);
                                Map<String, Object> newData = dc.getDocument().getData();
                                Double oldDateTime = (Double) oldData.get("Date_Time");
                                Double newDateTime = (Double) newData.get("Date_Time");
                                if (newDateTime > oldDateTime)
                                    sensors.put(sensorID, newData);
                            }
                            else
                                sensors.put(sensorID, dc.getDocument().getData());
                            break;
                        case REMOVED:
                            sensors.remove(dc.getDocument().get("Sensor_ID"));
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}

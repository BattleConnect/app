package com.cs495.battleconnect.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import com.cs495.battleconnect.R;
import com.cs495.battleconnect.holders.objects.Request;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity is for contributing data from the battlefield.
 */
public class InputActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        db = FirebaseFirestore.getInstance();
        configureSubmitButton();

        final Spinner tagSpinner = findViewById(R.id.inputTagSpinner);
        final Spinner conditionSpinner = findViewById(R.id.inputConditionSpinner);

        //this array determines if the condition dropdown should be shown, based on the tag selected from the tag dropdown.
        final int[] showCondition = getResources().getIntArray(R.array.showCondition);

        //hide the condition dropdown, only show it when it is necessary
        conditionSpinner.setVisibility(View.INVISIBLE);

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> tagSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tagList)) {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be used for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        tagSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagSpinnerArrayAdapter);

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Determine whether to show the condition dropdown or not, based on the tag selected.
                    if (showCondition[position] == 1)
                        conditionSpinner.setVisibility(View.VISIBLE);
                    else
                        conditionSpinner.setVisibility(View.INVISIBLE);
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> conditionSpinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.conditionList)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        conditionSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionSpinnerArrayAdapter);

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  Configures the button for submitting one's report.
     */
    private void configureSubmitButton(){
        Button submitButton = (Button) findViewById(R.id.inputSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    /**
     * Sends the data the user entered to Firebase.
     */
    private void sendData(){
        EditText commentBox = (EditText) findViewById(R.id.inputComment);
        String comment = commentBox.getText().toString();

        Spinner tagSpinner = (Spinner) findViewById(R.id.inputTagSpinner);
        String tag = tagSpinner.getSelectedItem().toString();

        Spinner conditionSpinner = (Spinner) findViewById(R.id.inputConditionSpinner);
        String condition = conditionSpinner.getSelectedItem().toString();

        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());

        Request request = new Request(tag, condition, time, comment);

        DocumentReference newReq = db.collection("requests").document();
        Map<String, Object> req = new HashMap<>();
        req.put("tag", request.getTag());
        req.put("condition", request.getCondition());
        req.put("time", FieldValue.serverTimestamp());
        req.put("comment", request.getComment());
        newReq.set(req);

        tagSpinner.setSelection(0);
        conditionSpinner.setSelection(0);
        commentBox.setText(null);
        displaySuccessToast();

    }

    /**
     * Notify the user when they have successfully submitted a report.
     */
    private void displaySuccessToast(){
        Context context = getApplicationContext();
        CharSequence toastText = "Request Submitted!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, toastText, duration);
        View view = toast.getView();
        view.setBackgroundColor(Color.parseColor("#5BC236"));
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

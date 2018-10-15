package com.cs495.battleelite.battleelite;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.sql.Timestamp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class InputScreen extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);
        db = FirebaseFirestore.getInstance();
        configureSubmitButton();
    }

    private void configureSubmitButton(){
        Button submitButton = (Button) findViewById(R.id.inputSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

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
        newReq.set(request);

        tagSpinner.setSelection(0);
        conditionSpinner.setSelection(0);
        commentBox.setText(null);
        displayToast();

    }

    private void displayToast(){
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

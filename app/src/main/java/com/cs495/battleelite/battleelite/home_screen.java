package com.cs495.battleelite.battleelite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class home_screen extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLoginStatus();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);




        configureButtons();
    }

    private void configureButtons(){
        Button viewButton = (Button) findViewById(R.id.view_button);
        Button inputButton = (Button) findViewById(R.id.input_button);
        Button mapButton = (Button) findViewById(R.id.map_button);
        Button alertsButton = (Button) findViewById(R.id.alerts_button);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, SensorActivity.class));
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, InputScreen.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, MapsActivity.class));
            }
        });

        alertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, NotificationActivity.class));
            }
        });
    }

    private  boolean checkLoginStatus(){
            firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) { //already logged in
            Toast.makeText(this, "Empty Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.updateCurrentUser(null);
            firebaseAuth.signOut();
            return true;
        }
        Intent myIntent = new Intent(home_screen.this, LoginActivity.class);
        home_screen.this.startActivityForResult(myIntent,1);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //setContentView(R.layout.activity_home_screen);
        if(resultCode == 0){// creates loop so you cant back out of login into main
            checkLoginStatus();
        }
    }

}

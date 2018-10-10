package com.cs495.battleelite.battleelite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                startActivity(new Intent(home_screen.this, SensorActivity.class));
            }
        });

        alertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, SensorActivity.class));
            }
        });
    }

}

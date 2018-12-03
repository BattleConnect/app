package com.cs495.battleelite.battleelite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class home_screen extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLoginStatus();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        configureButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case   R.id.actionClearCache:
                clearCache();
                return true;
            case   R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                return true;
            case   R.id.actionLogout:
                logout();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void configureButtons(){
        Button viewButton = (Button) findViewById(R.id.view_button);
        Button inputButton = (Button) findViewById(R.id.input_button);
        Button mapButton = (Button) findViewById(R.id.map_button);
        Button alertsButton = (Button) findViewById(R.id.alerts_button);
        Button forceButton = (Button) findViewById(R.id.soldier_button);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(home_screen.this, SensorActivity.class));
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(home_screen.this, InputScreen.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(home_screen.this, MapsActivity.class));
            }
        });

        alertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(home_screen.this, NotificationActivity.class));
            }
        });

        forceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(home_screen.this, ForcesActivity.class));
            }
        });
    }

    private  boolean checkLoginStatus(){
            firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) { //already logged in
            //Toast.makeText(this, "Empty Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.updateCurrentUser(firebaseAuth.getCurrentUser());
            return true;
        }
        Intent myIntent = new Intent(home_screen.this, LoginActivity.class);
        home_screen.this.startActivityForResult(myIntent,1);
        return true;
    }

    private void logout(){

        firebaseAuth.signOut();
        checkLoginStatus();
    }
    private void clearCache(){
        try {
            Context context = this;
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            Toast.makeText(this, "Error, cannot Delete cache", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
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

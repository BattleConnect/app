package com.cs495.battleconnect.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.cs495.battleconnect.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

/**
 * This activity manages the home screen.
 */
public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLoginStatus();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //Configure the toolbar
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

    /**
     * Handles toolbar actions.
     * @param item The toolbar action that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionClearCache:
                clearCache();
                return true;
            case R.id.actionLogout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Configures the buttons that lead to other activities.
     */
    private void configureButtons(){
        Button viewButton = findViewById(R.id.view_button);
        Button inputButton = findViewById(R.id.input_button);
        Button mapButton = findViewById(R.id.map_button);
        Button alertsButton = findViewById(R.id.alerts_button);
        Button forceButton = findViewById(R.id.soldier_button);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(HomeActivity.this, SensorActivity.class));
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(HomeActivity.this, InputActivity.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });

        alertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
            }
        });

        forceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
                startActivity(new Intent(HomeActivity.this, ForceActivity.class));
            }
        });
    }

    /**
     * Checks if the user is already logged in, if not, displays the login page.
     * @return
     */
    private  boolean checkLoginStatus(){
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) { //already logged in
            //Toast.makeText(this, "Empty Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.updateCurrentUser(firebaseAuth.getCurrentUser());
            return true;
        }
        Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
        HomeActivity.this.startActivityForResult(myIntent,1);
        return true;
    }

    /**
     * Logs the user out.
     */
    private void logout(){

        firebaseAuth.signOut();
        checkLoginStatus();
    }

    /**
     * Clears the app's cache. Sometimes the cache needs to be cleared since the app will cache data from Firebase, which may no longer be in Firebase.
     */
    private void clearCache(){
        try {
            Context context = this;
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            Toast.makeText(this, "Error, cannot clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes directories/files in the app's cache.
     * @param dir
     * @return
     */
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
        if(resultCode == 0){// creates loop so you cant back out of login into main
            checkLoginStatus();
        }
    }

}

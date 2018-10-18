package com.cs495.battleelite.battleelite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startActivityForResult(new Intent(MainActivity.this, home_screen.class), 0);
//        Intent loginIntent = new Intent(MainActivity.this,  LoginActivity.class);
//        MainActivity.this.startActivity(loginIntent);

//        Intent myIntent = new Intent(MainActivity.this, home_screen.class);
//        MainActivity.this.startActivity(myIntent);
    }
    private FirebaseAuth firebaseAuth;
    public void setAuth( FirebaseAuth x){
        this.firebaseAuth = x;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //this.firebaseAuth.signOut();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                if(resultCode == 1){
                    Intent myIntent = new Intent(MainActivity.this, home_screen.class);
                    MainActivity.this.startActivity(myIntent);

                }
                break;
            case 1:
                //etc...
                break;
        }
    }


}

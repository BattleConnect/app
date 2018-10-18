package com.cs495.battleelite.battleelite;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private  Button loginB;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginB = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //configureButtons();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {//already logged in
            success();
        }

        loginB.setOnClickListener(this);
    }

    private void  login(){
       String email = editTextEmail.getText().toString().trim();
       String password = editTextPassword.getText().toString().trim();

       if(TextUtils.isEmpty(email)){
           Toast.makeText(this, "Empty Email", Toast.LENGTH_SHORT).show();
           setResult(Activity.RESULT_OK, null);

       }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    success();

                }
                else{
                    fail();
                }
            }
        });
    }
    private void success(){
        Toast.makeText(this, "You have successfully logged in.", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultCode", 0);
        setResult(0, resultIntent);
    }
    private void fail(){
        Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view){
        login();
    }

}

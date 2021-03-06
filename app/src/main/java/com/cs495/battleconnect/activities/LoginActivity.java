package com.cs495.battleconnect.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cs495.battleconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the activity that handles user authentication.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final String USERS = "users";
    private  Button loginB;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginB = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        //configureButtons();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {//already logged in
            success();
        }

        loginB.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onDestroy() {
        Intent returnIntent = new Intent();
        setResult(0, returnIntent);
        super.onDestroy();

    }

    /**
     * Attempts to login the user.
     */
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
                    Intent returnIntent = new Intent();
                    setResult(1, returnIntent);
                    finish();
                    return;
                }
                else{
                    fail();
                }
            }
        });
    }

    /**
     * Display a toast when the user has successfully logged in.
     */
    private void success(){
        Toast.makeText(this, "You have successfully logged in.", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultCode", 0);
        setResult(0, resultIntent);

        //store the user id for notification purposes
        storeUUID();
    }

    /**
     * Remember the user so that they don't have to login again.
     */
    private void storeUUID() {
        //store the new token in the database
        if(firebaseAuth.getCurrentUser() != null) {
            Map<String, Object> data = new HashMap<>();
            String uuid = firebaseAuth.getCurrentUser().getUid();
            String token = FirebaseInstanceId.getInstance().getToken();
            data.put("id", token);

            db.collection(USERS).document(uuid)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Updated device id!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating device id!", e);
                        }
                    });
        }
    }

    /**
     * Show a toast letting the user know they failed to login.
     */
    private void fail(){
        Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view){
        login();
    }

}

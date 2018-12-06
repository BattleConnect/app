package com.cs495.battleconnect;

import android.support.test.runner.AndroidJUnit4;

import com.cs495.battleconnect.adapters.ForceAdapter;
import com.cs495.battleconnect.adapters.SensorAdapter;
import com.cs495.battleconnect.fragments.ForceRecyclerViewFragment;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;
import com.cs495.battleconnect.responses.ForceResponse;
import com.cs495.battleconnect.responses.SensorResponse;
import com.cs495.battleconnect.activities.LoginActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Before
    public void setUp(){
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testFireBaseToken(){
        if(firebaseAuth.getCurrentUser() != null) {
            assertNotNull(FirebaseInstanceId.getInstance());
            //assertNotNull(firebaseAuth.getCurrentUser().getUid());
        }

        else{
            fail();
        }

    }
    @Test
    public void testFireBaseUuid(){
        if(firebaseAuth.getCurrentUser() != null) {
            //assertNotNull(FirebaseInstanceId.getInstance());
            assertNotNull(firebaseAuth.getCurrentUser().getUid());
        }

        else{
            fail();
        }

    }

}

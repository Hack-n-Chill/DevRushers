package com.appdev_soumitri.humbirds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.appdev_soumitri.humbirds.models.MusicActivity;
import com.appdev_soumitri.humbirds.services.NetworkConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 1000;

    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.hide();

        boolean isConnected = new NetworkConnection(this).isConnected();

        // check if current user is logged in , if yes then take him to Homepage, else take him to LoginPage
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null)
        {
            if(!isConnected) {
                Toast.makeText(this, "Please ensure you have internet connection !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LauncherActivity.this,NoInternetActivity.class));
                finish();
            }

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
            final String uID=user.getUid();

            Query checkUser = reference.orderByChild("userID").equalTo(uID);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()) {

                        String _name=snapshot.child(uID).child("name").getValue(String.class);
                        int _age= Objects.requireNonNull(snapshot.child(uID).child("age").getValue(Integer.class));
                        String _gender = snapshot.child(uID).child("gender").getValue(String.class);
                        String _email = snapshot.child(uID).child("email").getValue(String.class);
                        assert _name != null;
                        Log.d("name:",_name);
                        Log.d("age:",_age+" ");
                        assert _gender != null;
                        Log.d("gender",_gender);

                        Intent intent=new Intent(LauncherActivity.this,MainActivity.class);
                        intent.putExtra("name",_name);
                        intent.putExtra("email",_email);
                        intent.putExtra("age",_age);
                        intent.putExtra("gender",_gender);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

//                        startActivity(new Intent(LauncherActivity.this, MusicActivity.class));
//                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    finish();
                }
            }, SPLASH_SCREEN_TIME_OUT);
        }
    }
}
package com.example.chato;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button odjavi = findViewById(R.id.odjavi);
        Button najdiOsebo = findViewById(R.id.najdiOsebo);


        najdiOsebo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), NajdiOsebeActivity.class));
            }
        });


        odjavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent gatekeep = new Intent(getApplicationContext(), LoginActivity.class);
                gatekeep.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gatekeep);
                finish();
                return;
            }
        });

        getPermissions();
    }

    private void getPermissions() {
        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);
    }
}
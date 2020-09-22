package com.example.chato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private EditText fonska, koda;
    private TextView success;
    private Button poslji;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    String sVerifikacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        uspesnoPrijavljen();


        fonska = findViewById(R.id.fonska);
        koda = findViewById(R.id.koda);
        success = findViewById(R.id.poslano);
        poslji = findViewById(R.id.poslji);

        poslji.setOnClickListener(new View.OnClickListener(){
            boolean vidno;
            @Override
            public void onClick(View v){
                if(sVerifikacija!= null){
                    vidno =!vidno;
                    success.setVisibility(vidno ? View.VISIBLE: View.GONE);

                    jeFonskaVrediZKodo();

                }
                else{
                startFonskaVerifikacija();
                }
            }
        });

        callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                  prijaviZfonsko(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
            @Override
            public void onCodeSent (String verifikacija, PhoneAuthProvider.ForceResendingToken spetPoslji){
                super.onCodeSent(verifikacija, spetPoslji);

                sVerifikacija = verifikacija;
                poslji.setText("Verify code");
            }
        };
    }

    private void jeFonskaVrediZKodo(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sVerifikacija, koda.getText().toString());
        prijaviZfonsko(credential);

    }

    private void prijaviZfonsko(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){{
                    uspesnoPrijavljen();
                }}
            }
        });
    }

    private void uspesnoPrijavljen() {
        FirebaseUser uporabnik = FirebaseAuth.getInstance().getCurrentUser();
        if(uporabnik!=null){

            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }
    }

    private void startFonskaVerifikacija() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fonska.getText().toString(),
                120,
                TimeUnit.SECONDS,
                this,
                 callback);
       }


}
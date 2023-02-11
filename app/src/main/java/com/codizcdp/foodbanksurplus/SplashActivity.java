package com.codizcdp.foodbanksurplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codizcdp.foodbanksurplus.customer.CustomerMainActivity;
import com.codizcdp.foodbanksurplus.provider.ProviderMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    boolean customer = false;
    boolean provider = false;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Log.d(TAG, "onCreate: User Found");
            String email = user.getEmail();

            Log.d(TAG, "onCreate: " + email);
            firebaseFirestore.collection("Customer")
                    .document(email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                                        startActivity(new Intent(SplashActivity.this, CustomerMainActivity.class));
                                        finish();
                                    }
                                }, 1000);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            customer = false;
                        }
                    });

            firebaseFirestore.collection("Provider").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {

                                startActivity(new Intent(SplashActivity.this, ProviderMainActivity.class));
                                finish();
                            }
                        }, 1000);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}

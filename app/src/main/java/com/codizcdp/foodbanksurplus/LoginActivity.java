package com.codizcdp.foodbanksurplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codizcdp.foodbanksurplus.customer.CustomerMainActivity;
import com.codizcdp.foodbanksurplus.provider.ProviderMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextView register_here;

    private Button btnLogin;

    private TextInputEditText etEmail, etPass;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();

        register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CustomerRegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void initialize() {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        register_here = findViewById(R.id.register);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);

        btnLogin = findViewById(R.id.btnLogin);
    }

    private void getData() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (email.equals("") || pass.equals("")) {

            if (pass.equals("")) {
                etPass.setError("Required");
            }

            if (email.equals("")) {
                etEmail.setError("Required");
            }
        } else {
            etEmail.setError(null);
            etPass.setError(null);
            signInWithEmailAndPassword(email, pass);
        }
    }

    private void signInWithEmailAndPassword(String email, String pass) {
        firebaseFirestore.collection("Customer")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            auth.signInWithEmailAndPassword(email, pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    }
                });
        firebaseFirestore.collection("Provider")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            auth.signInWithEmailAndPassword(email, pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(LoginActivity.this, ProviderMainActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
}
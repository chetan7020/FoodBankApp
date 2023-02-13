package com.codizcdp.foodbanksurplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codizcdp.foodbanksurplus.customer.CustomerMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "CustomerRegisterActivity";

    private TextView provider_register, login;
    private TextInputEditText et_confirm_pass, et_pass, et_email, et_name;

    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private void initialize() {
        provider_register = findViewById(R.id.provider_register);
        login = findViewById(R.id.login);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);
        et_pass = findViewById(R.id.et_pass);
        et_email = findViewById(R.id.et_username);
        et_name = findViewById(R.id.et_name);

        btnSignUp = findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getFiledData() {

        Log.d(TAG, "getFiledData: ");

        String name, email, confirmPass, pass;

        email = et_email.getText().toString().trim();
        name = et_name.getText().toString().trim();
        confirmPass = et_confirm_pass.getText().toString().trim();
        pass = et_pass.getText().toString().trim();

        if (name.equals("") || confirmPass.equals("") || pass.equals("")) {

            if (name.equals("")) {
                et_name.setError("Required");
            }

            if (email.equals("")) {
                et_email.setError("Required");
            }

            if (confirmPass.equals("")) {
                et_confirm_pass.setError("Required");
            }

            if (pass.equals("")) {
                et_pass.setError("Required");
            }

        } else {
            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Password are not equal", Toast.LENGTH_SHORT).show();
            } else {
                et_email.setError(null);
                et_confirm_pass.setError(null);
                et_pass.setError(null);
                et_name.setError(null);
                signUpWithEmailAndPassword(email, pass, name);
            }
        }
    }

    private void init() {
        initialize();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFiledData();
                Log.d(TAG, "onClick: Button Clicked");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        provider_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegisterActivity.this, ProviderRegister.class);
                startActivity(intent);
            }
        });

    }

    private void signUpWithEmailAndPassword(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String id = createID();
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", id);
                            data.put("name", name);
                            data.put("email", email);
                            data.put("order", "0");

                            firebaseFirestore
                                    .collection("Customer")
                                    .document(email)
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(CustomerRegisterActivity.this, CustomerMainActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerRegisterActivity.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(CustomerRegisterActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String createID() {
        String id = "";
        id = String.valueOf(System.currentTimeMillis());
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        init();

    }
}
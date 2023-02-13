package com.codizcdp.foodbanksurplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codizcdp.foodbanksurplus.provider.ProviderMainActivity;
import com.codizcdp.foodbanksurplus.provider.Verhoeff;
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

public class ProviderRegister extends AppCompatActivity {
    private TextInputEditText etName, etFoodCompanyName, etPhoneNumber, etGmail, etPass, etAadharNumber, etLocation;
    private Button btnSignUp;
    private static final String TAG = "ProviderRegister";
    private TextView tvLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private boolean flag = false;

    private void initialize() {
        etName = findViewById(R.id.etName);
        etFoodCompanyName = findViewById(R.id.etFoodCompanyName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etGmail = findViewById(R.id.etGmail);
        etPass = findViewById(R.id.etPass);
        etAadharNumber = findViewById(R.id.etAadhar);
        etLocation = findViewById(R.id.etLocation);

        tvLogin = findViewById(R.id.tvLoginNow);

        btnSignUp = findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void init() {
        initialize();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFiledData();
            }
        });
    }

    private void getFiledData() {
        String name, companyName, phoneNumber, gmail, pass, aadhar, location;

        name = etName.getText().toString().trim();
        companyName = etFoodCompanyName.getText().toString().trim();
        phoneNumber = etPhoneNumber.getText().toString().trim();
        gmail = etGmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        location = etLocation.getText().toString().trim();

        aadhar = etAadharNumber.getText().toString().trim();

        if (name.equals("") || companyName.equals("") || phoneNumber.equals("") || gmail.equals("") || pass.equals("") || aadhar.equals("")|| location.equals("")) {

            if (name.equals("")) {
                etName.setError("Required");
            }

            if (companyName.equals("")) {
                etFoodCompanyName.setError("Required");
            }

            if (phoneNumber.equals("")) {
                etPhoneNumber.setError("Required");
            }

            if (gmail.equals("")) {
                etGmail.setError("Required");
            }

            if (pass.equals("")) {
                etPass.setError("Required");
            }

            if (aadhar.equals("")) {
                etAadharNumber.setError("Required");
            }

            if (location.equals("")) {
                etAadharNumber.setError("Required");
            }


        } else {
            etName.setError(null);
            etFoodCompanyName.setError(null);
            etGmail.setError(null);
            etPhoneNumber.setError(null);
            etPass.setError(null);
            etAadharNumber.setError(null);
            etLocation.setError(null);

            flag = Verhoeff.ValidateVerhoeff(aadhar);

            if (flag) {
                signUpWithEmailAndPassword(gmail, pass, name, companyName, phoneNumber, aadhar, location);
            }else{
                Toast.makeText(this, "Invalid Aadhar Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String createID() {
        String id = "";
        id = String.valueOf(System.currentTimeMillis());
        return id;
    }

    private void signUpWithEmailAndPassword(String email, String password, String name, String companyName, String phoneNumber, String aadhar, String location) {
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
                            data.put("companyName", companyName);
                            data.put("phoneNumber", phoneNumber);
                            data.put("aadhar", aadhar);
                            data.put("order", "0");
                            data.put("location", location);
                                data.put("lat", "null");
                            data.put("lang", "null");

                            firebaseFirestore
                                    .collection("Provider")
                                    .document(email)
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(ProviderRegister.this, ProviderMainActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProviderRegister.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(ProviderRegister.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_register);

        init();
    }
}
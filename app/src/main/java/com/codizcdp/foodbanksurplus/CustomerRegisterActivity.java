package com.codizcdp.foodbanksurplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerRegisterActivity extends AppCompatActivity {
    TextView provider_register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        provider_register = findViewById(R.id.provider_register);
        login = findViewById(R.id.login);

        provider_register = findViewById(R.id.provider_register);
        login = findViewById(R.id.login);

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
}
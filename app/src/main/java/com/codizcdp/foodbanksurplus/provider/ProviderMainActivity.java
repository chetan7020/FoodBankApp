package com.codizcdp.foodbanksurplus.provider;

import android.os.Bundle;

import com.codizcdp.foodbanksurplus.R;
import com.codizcdp.foodbanksurplus.databinding.ActivityProviderMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ProviderMainActivity extends AppCompatActivity {

    private ActivityProviderMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProviderMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_feedback, R.id.navigation_order, R.id.navigation_food, R.id.navigation_notice, R.id.navigation_profile)
                .build();
        NavController navController = Navigation
                .findNavController(this, R.id.nav_host_fragment_activity_provider_main);
        NavigationUI
                .setupWithNavController(binding.navView, navController);
    }

}
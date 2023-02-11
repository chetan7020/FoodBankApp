package com.codizcdp.foodbanksurplus.customer;

import android.os.Bundle;

import com.codizcdp.foodbanksurplus.R;
import com.codizcdp.foodbanksurplus.databinding.ActivityCustomerMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class CustomerMainActivity extends AppCompatActivity {

    private ActivityCustomerMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.customer_navigation_ordered_food, R.id.customer_navigation_provider, R.id.customer_navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_customer_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}
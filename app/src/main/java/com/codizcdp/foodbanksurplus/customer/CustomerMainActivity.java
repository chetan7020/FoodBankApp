package com.codizcdp.foodbanksurplus.customer;

import android.os.Bundle;

import com.codizcdp.foodbanksurplus.R;
import com.codizcdp.foodbanksurplus.customer.ui.CustomerOrderedFoodFragment;
import com.codizcdp.foodbanksurplus.customer.ui.CustomerProfileFragment;
import com.codizcdp.foodbanksurplus.customer.ui.CustomerProviderFragment;
import com.codizcdp.foodbanksurplus.databinding.ActivityCustomerMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
        navView.setOnNavigationItemSelectedListener(navListener);
        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomerProviderFragment()).commit();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.provider) {
            selectedFragment = new CustomerProviderFragment();
        } else if (itemId == R.id.order_food) {
            selectedFragment = new CustomerOrderedFoodFragment();
        } else if (itemId == R.id.profile) {
            selectedFragment = new CustomerProfileFragment();
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
        }
        return true;
    };

}
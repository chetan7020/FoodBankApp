package com.codizcdp.foodbanksurplus.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.R;

public class CustomerUpdateProfileFragment extends Fragment {
    private View view;

    private void init() {
        initialize();
    }

    private void initialize() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_update_profile, container, false);

        init();

        return view;
    }
}
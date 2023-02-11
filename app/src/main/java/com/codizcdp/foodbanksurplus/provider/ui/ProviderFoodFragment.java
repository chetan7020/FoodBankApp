package com.codizcdp.foodbanksurplus.provider.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProviderFoodFragment extends Fragment {

    private View view;

    private void init() {
        initialize();

    }

    private void initialize() {

        FloatingActionButton plusButton;
        plusButton = view.findViewById(R.id.addFoodFloatingActionBtn);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Chetan, add the logic to load/replace the new fragment of adding new food
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provider_fragment_food, container, false);

        init();

        return view;
    }
}
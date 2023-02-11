package com.codizcdp.foodbanksurplus.provider.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.LoginActivity;
import com.codizcdp.foodbanksurplus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class ProviderFoodFragment extends Fragment {

    private View view;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    String [] items = {"Cooked Food", "Un-Cooked Food"};
    ArrayAdapter<String> adapterItems;

    private void init() {
        initialize();

        adapterItems = new ArrayAdapter<String>(getActivity(),R.layout.items_list,items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), items, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialize() {

        textInputLayout = view.findViewById(R.id.menu_drop);
        autoCompleteTextView = view.findViewById(R.id.drop_items);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provider_fragment_food, container, false);

        init();

        return view;
    }
}
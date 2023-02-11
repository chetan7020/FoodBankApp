package com.codizcdp.foodbanksurplus.provider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.R;

public class ProviderSendVerificationRequestFragment extends Fragment {

    private View view;

    private void init() {
        initialize();

    }

    private void initialize() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_provider_send_verification_request, container, false);

        init();

        return view;
    }
}
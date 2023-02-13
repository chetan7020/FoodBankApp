package com.codizcdp.foodbanksurplus.customer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerProviderFragment extends Fragment {

    private View view;
    private LinearLayout llData;

    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();
    }

    private void initialize() {
        llData = view.findViewById(R.id.llData);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getProviders(){
        firebaseFirestore.collection("Provider")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_provider, container, false);

        init();

        return view;
    }
}
package com.codizcdp.foodbanksurplus.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.LoginActivity;
import com.codizcdp.foodbanksurplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CustomerProfileFragment extends Fragment {

    private View view;
    private Button btnChangePass, btnLogout;

    private TextView tvName, tvID, tvOrder, tvEmail;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private void initialize() {
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvID = view.findViewById(R.id.tvID);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvOrder = view.findViewById(R.id.tvOrder);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setUpProfile() {
        String email = auth.getCurrentUser().getEmail();

        firebaseFirestore.collection("Customer")
                .document(email)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot.exists()) {
                            String id = documentSnapshot.get("id").toString();
                            String name = documentSnapshot.get("name").toString();
                            String email = documentSnapshot.get("email").toString();
                            String order = documentSnapshot.get("order").toString();

                            tvID.setText(id);
                            tvName.setText(name);
                            tvEmail.setText(email);
                            tvOrder.setText(order);
                        }
                    }
                });

    }


    private void init() {
        initialize();

        setUpProfile();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Yet to build", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_profile, container, false);

        init();

        return view;
    }
}
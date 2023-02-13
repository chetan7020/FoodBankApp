package com.codizcdp.foodbanksurplus.provider.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ProviderProfileFragment extends Fragment {

    private static final String TAG = "ProviderProfileFragment";

    private View view;

    private String companyName, name, phoneNumber;

    private TextView tvID, tvName, tvRestaurantName, tvEmail, tvPhoneNumber, tvAadhdar, tvNumberFoodDonate;

    private Button btnUpdateProfile, btnLogout;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private void initialize() {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvID = view.findViewById(R.id.tvID);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRestaurantName = view.findViewById(R.id.tvRestaurantName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvAadhdar = view.findViewById(R.id.tvAadhdar);
        tvNumberFoodDonate = view.findViewById(R.id.tvNumberFoodDonate);
    }

    private void init() {
        initialize();

        setupProfile();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void updateProfile() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_provider_update_profile);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView etName, etCompanyName, etPhoneNumber;

        etName = dialog.findViewById(R.id.etName);
        etCompanyName = dialog.findViewById(R.id.etCompanyName);
        etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);

        etName.setText(name);
        etCompanyName.setText(companyName);
        etPhoneNumber.setText(phoneNumber);

        Button btnUpdate;

        btnUpdate = dialog.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = auth.getCurrentUser().getEmail();
                name = etName.getText().toString();
                companyName = etCompanyName.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();

                Log.d(TAG, "onClick: "+email);

                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("companyName", companyName);
                data.put("phoneNumber", phoneNumber);

                firebaseFirestore.collection("Provider")
                        .document(email)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

        dialog.show();
    }

    private void setupProfile() {
        String email = auth.getCurrentUser().getEmail();

        firebaseFirestore.collection("Provider")
                .document(email)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot.exists()) {
                            String id = documentSnapshot.get("id").toString();
                            companyName = documentSnapshot.get("companyName").toString();
                            String aadhar = documentSnapshot.get("aadhar").toString();
                            String email = documentSnapshot.get("email").toString();
                            name = documentSnapshot.get("name").toString();
                            phoneNumber = documentSnapshot.get("phoneNumber").toString();
                            String order = documentSnapshot.get("order").toString();

                            tvID.setText(id);
                            tvRestaurantName.setText(companyName);
                            tvAadhdar.setText(aadhar);
                            tvEmail.setText(email);
                            tvName.setText(name);
                            tvPhoneNumber.setText(phoneNumber);
                            tvNumberFoodDonate.setText(order);
                        }
                    }
                });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provider_fragment_profile, container, false);

        init();

        return view;
    }
}
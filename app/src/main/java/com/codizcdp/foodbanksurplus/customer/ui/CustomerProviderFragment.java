package com.codizcdp.foodbanksurplus.customer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codizcdp.foodbanksurplus.R;
import com.codizcdp.foodbanksurplus.customer.provider.CustomerProviderFoodFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerProviderFragment extends Fragment {

    private static final String TAG = "ProviderFoodFragment";

    private LinearLayout llData;

    private View view;

    private FirebaseFirestore firebaseFirestore;

    private void replaceFragment(Fragment fragment, String email, String providerName) {
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("providerName", providerName);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initialize() {
        llData = view.findViewById(R.id.llData);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void init() {
        initialize();

        displayProvider();

    }

    private void displayProvider() {
        Log.d(TAG, "displayProvider: Reached");
        firebaseFirestore.collection("Provider")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            Log.d(TAG, "onEvent: " + dc.getDocument().getData());
                            String companyName = dc.getDocument().getData().get("companyName").toString();
                            String email = dc.getDocument().getData().get("email").toString();
                            String id = dc.getDocument().getData().get("id").toString();
                            String location = dc.getDocument().getData().get("location").toString();
                            String available = dc.getDocument().getData().get("available").toString();
                            String timeFrom = dc.getDocument().getData().get("timeFrom").toString();
                            String timeTo = dc.getDocument().getData().get("timeTo").toString();
                            String providerName = dc.getDocument().getData().get("name").toString();
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "onEvent: ADDED" + dc.getDocument().getData());
                                    createCard(id, providerName, companyName, available, email, timeFrom, timeTo, location);
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "onEvent: MODIFIED" + dc.getDocument().getData());
                                    updateProvider(id, providerName, companyName, available, email, timeFrom, timeTo, location);
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "onEvent: REMOVED" + dc.getDocument().getData());
                                    for (int i = 0; i < llData.getChildCount(); i++) {

                                        TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);

                                        String firebase_id = tvID.getText().toString().trim();

                                        if (firebase_id.equals(id)) {
                                            llData.removeView(llData.getChildAt(i));
                                        }
                                    }
                                    break;
                            }

                        }
                    }
                });
    }

    private void updateProvider(String id, String providerName, String companyName, String available, String email, String timeFrom, String timeTo, String location) {
        for (int i = 0; i < llData.getChildCount(); i++) {
            TextView tvID, tvCompanyName, tvAvailable, tvEmail, tvLocation, tvTime, tvProviderName;

            tvID = llData.getChildAt(i).findViewById(R.id.tvID);
            tvCompanyName = llData.getChildAt(i).findViewById(R.id.tvCompanyName);
            tvAvailable = llData.getChildAt(i).findViewById(R.id.tvAvailable);
            tvEmail = llData.getChildAt(i).findViewById(R.id.tvEmail);
            tvLocation = llData.getChildAt(i).findViewById(R.id.tvLocation);
            tvTime = llData.getChildAt(i).findViewById(R.id.tvTime);
            tvProviderName = llData.getChildAt(i).findViewById(R.id.tvProviderName);


            if (tvID.getText().toString().trim().equals(id)) {
                Log.d(TAG, "updateProvider: " + id);
                tvCompanyName.setText(companyName);
                tvAvailable.setText(available);
                tvEmail.setText(email);
                tvLocation.setText(location);
                tvTime.setText(timeFrom + "-" + timeTo);
                tvProviderName.setText(providerName);

            }

        }
    }

    private void createCard(String id, String providerName, String companyName, String available, String email, String timeFrom, String timeTo, String location) {
        View providerView = getLayoutInflater().inflate(R.layout.customer_provider_layout, null, false);

        TextView tvID, tvCompanyName, tvAvailable, tvEmail, tvLocation, tvTime, tvProviderName;
        LinearLayout llCPLayout;


        tvID = providerView.findViewById(R.id.tvID);
        tvCompanyName = providerView.findViewById(R.id.tvCompanyName);
        tvAvailable = providerView.findViewById(R.id.tvAvailable);
        tvEmail = providerView.findViewById(R.id.tvEmail);
        tvLocation = providerView.findViewById(R.id.tvLocation);
        tvTime = providerView.findViewById(R.id.tvTime);
        tvProviderName = providerView.findViewById(R.id.tvProviderName);

        llCPLayout = providerView.findViewById(R.id.llCPLayout);

        tvID.setText(id);
        tvProviderName.setText(providerName);
        tvCompanyName.setText(companyName);
        tvAvailable.setText(available);
        tvEmail.setText(email);
        tvTime.setText(timeFrom + "-" + timeTo);
        tvLocation.setText(location);
        llCPLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmail.getText().toString();
                String providerName = tvProviderName.getText().toString();
                replaceFragment(new CustomerProviderFoodFragment(), email, providerName);
            }
        });

        llData.addView(providerView);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_provider, container, false);

        Log.d(TAG, "onCreateView: ProviderFoodFragment");

        init();

        return view;
    }
}
package com.codizcdp.foodbanksurplus.customer.provider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codizcdp.foodbanksurplus.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CustomerProviderFoodFragment extends Fragment {

    private static final String TAG = "CustomerProviderFoodFragment";
    private LinearLayout llData;
    private View view;
    private FirebaseFirestore firebaseFirestore;
    String email;
    private void initialize() {
        llData = view.findViewById(R.id.llData);

        firebaseFirestore = FirebaseFirestore.getInstance();

        email = getArguments().getString("email");
    }

    private void init() {
        initialize();

        displayFood();
    }

    private void displayFood() {
        firebaseFirestore.collection( email+ "_food")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            String id = dc.getDocument().getData().get("id").toString();
                            String dishName = dc.getDocument().getData().get("dishName").toString();
                            String expiryTime = dc.getDocument().getData().get("expiryTime").toString();
                            String category = dc.getDocument().getData().get("category").toString();
                            String type = dc.getDocument().getData().get("type").toString();
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "onEvent: ADDED"+dc.getDocument().getData());
                                    createCard(id, dishName, expiryTime, category, type);
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "onEvent: MODIFIED"+dc.getDocument().getData());
                                    updateFood(id, dishName, expiryTime, category, type);
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "onEvent: REMOVED"+dc.getDocument().getData());
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

    private void updateFood(String id, String dishName, String expiryTime, String category, String type) {
        for (int i = 0; i < llData.getChildCount(); i++) {

            TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);
            TextView tvFoodTitle = llData.getChildAt(i).findViewById(R.id.tvFoodTitle);
            TextView tvExpiryTime = llData.getChildAt(i).findViewById(R.id.tvExpiryTime);
            TextView tvCategory = llData.getChildAt(i).findViewById(R.id.tvCategory);
            TextView tvType = llData.getChildAt(i).findViewById(R.id.tvType);


            if (tvID.getText().toString().trim().equals(id)) {
                tvFoodTitle.setText(dishName);
                tvExpiryTime.setText(expiryTime);
                tvCategory.setText(category);
                tvType.setText(type);

                Toast.makeText(getActivity(), "Food Data Updated", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void createCard(String id, String dishName, String expiryTime, String category, String type) {
        View foodView = getLayoutInflater().inflate(R.layout.layout_for_customer_all_food, null, false);

        TextView tvID, tvFoodTitle, tvExpiryTime, tvCategory, tvType;


        tvID = foodView.findViewById(R.id.tvID);
        tvFoodTitle = foodView.findViewById(R.id.tvFoodTitle);
        tvExpiryTime = foodView.findViewById(R.id.tvExpiryTime);
        tvCategory = foodView.findViewById(R.id.tvCategory);
        tvType = foodView.findViewById(R.id.tvType);

        tvID.setText(id);
        tvFoodTitle.setText(dishName);
        tvExpiryTime.setText(expiryTime);
        tvCategory.setText(category);
        tvType.setText(type);

        llData.addView(foodView);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_provider_food, container, false);

        Log.d(TAG, "onCreateView: ProviderFoodFragment");

        init();

        return view;
    }
}
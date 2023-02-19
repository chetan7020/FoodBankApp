package com.codizcdp.foodbanksurplus.provider.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codizcdp.foodbanksurplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProviderFoodFragment extends Fragment {

    private static final String TAG = "ProviderFoodFragment";

    private LinearLayout llData;

    private View view;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private FloatingActionButton fabAdd;

    private void initialize() {
        fabAdd = view.findViewById(R.id.fabAdd);
        llData = view.findViewById(R.id.llData);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void init() {
        initialize();

        displayFood();

        fabAdd.setOnClickListener(view -> setupDialog());

    }

    private void displayFood() {
        firebaseFirestore.collection(firebaseAuth.getCurrentUser().getEmail() + "_food")
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

    private void deleteFood(String id) {
        Log.d(TAG, "deleteFood: " + id);
        firebaseFirestore
                .collection(firebaseAuth.getCurrentUser().getEmail() + "_food")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Food Data Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

            }

        }
    }

    private void createCard(String id, String dishName, String expiryTime, String category, String type) {
        View foodView = getLayoutInflater().inflate(R.layout.layout_for_food_card, null, false);

        TextView tvID, tvFoodTitle, tvExpiryTime, tvCategory, tvType;

        Button btnEdit, btnDelete;

        btnEdit = foodView.findViewById(R.id.btnEdit);
        btnDelete = foodView.findViewById(R.id.btnDelete);


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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood(tvID.getText().toString());
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.fragment_provider_food_edit);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                String clicked_id = tvID.getText().toString();
                String clicked_dishName = tvFoodTitle.getText().toString();
                String clicked_expiryTime = tvExpiryTime.getText().toString();
                String clicked_category = tvCategory.getText().toString();
                String clicked_type = tvType.getText().toString();

                TextView tvFoodTitle = dialog.findViewById(R.id.tvFoodTitle);
                TextView tvExpiryTime = dialog.findViewById(R.id.tvExpiryTime);
                TextView tvCategory = dialog.findViewById(R.id.tvCategory);
                TextView tvType = dialog.findViewById(R.id.tvType);

                tvFoodTitle.setText(clicked_dishName);
                tvExpiryTime.setText(clicked_expiryTime);
                tvCategory.setText(clicked_category);
                tvType.setText(clicked_type);

                Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("dishName", tvFoodTitle.getText().toString());
                        data.put("expiryTime", tvExpiryTime.getText().toString());
                        data.put("category", tvCategory.getText().toString());
                        data.put("type", tvType.getText().toString());

                        firebaseFirestore
                                .collection(firebaseAuth.getCurrentUser().getEmail() + "_food")
                                .document(clicked_id)
                                .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Food Data Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        llData.addView(foodView);
    }

    private void setupDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_add_new_food);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextInputEditText etDishName, etExpiryTime, etCategory, etType;
        Button btnAdd;

        etDishName = dialog.findViewById(R.id.etDishName);
        etExpiryTime = dialog.findViewById(R.id.etExpiryTime);
        etCategory = dialog.findViewById(R.id.etCategory);
        etType = dialog.findViewById(R.id.etType);

        btnAdd = dialog.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAdd.setEnabled(false);
                String id, dishName, expiryTime, category, type;

                id = createID();
                dishName = etDishName.getText().toString();
                expiryTime = etExpiryTime.getText().toString();
                category = etCategory.getText().toString();
                type = etType.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("dishName", dishName);
                data.put("expiryTime", expiryTime);
                data.put("category", category);
                data.put("type", type);

                firebaseFirestore
                        .collection(firebaseAuth.getCurrentUser().getEmail() + "_food")
                        .document(id)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Food Added", Toast.LENGTH_SHORT).show();
                                btnAdd.setEnabled(true);
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        dialog.show();

    }

    public String createID() {
        String id = "";
        id = String.valueOf(System.currentTimeMillis());
        return id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provider_fragment_food, container, false);

        Log.d(TAG, "onCreateView: ProviderFoodFragment");

        init();

        return view;
    }
}
package com.codizcdp.foodbanksurplus.provider.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ProviderNoticeFragment extends Fragment {

    private static final String TAG = "ProviderNoticeFragment";

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

        displayNotice();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupDialog();
            }
        });

    }

    private void displayNotice() {
        firebaseFirestore.collection(firebaseAuth.getCurrentUser().getEmail() + "_notice")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            String id = dc.getDocument().getData().get("id").toString();
                            String title = dc.getDocument().getData().get("title").toString();
                            String description = dc.getDocument().getData().get("description").toString();
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "onEvent: ADDED"+dc.getDocument().getData());
                                    createCard(id, title, description);
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "onEvent: MODIFIED"+dc.getDocument().getData());
                                    updateNotice(id, title, description);
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
                .collection(firebaseAuth.getCurrentUser().getEmail() + "_notice")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Notice Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateNotice(String id, String title, String description) {
        Log.d(TAG, "updateNotice: Reached");
        for (int i = 0; i < llData.getChildCount(); i++) {

            TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);
            TextView tvTitle = llData.getChildAt(i).findViewById(R.id.tvTitle);
            TextView tvDisc = llData.getChildAt(i).findViewById(R.id.tvDisc);


            if (tvID.getText().toString().trim().equals(id)) {
                tvTitle.setText(title);
                tvDisc.setText(description);

                Toast.makeText(getActivity(), "Food Data Updated", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void createCard(String id, String title, String description) {
        View noticeView = getLayoutInflater().inflate(R.layout.layout_for_all_notices, null, false);

        TextView tvID, tvTitle, tvDisc;

        Button btnEdit, btnDelete;

        btnEdit = noticeView.findViewById(R.id.btnEdit);
        btnDelete = noticeView.findViewById(R.id.btnDelete);


        tvID = noticeView.findViewById(R.id.tvID);
        tvTitle = noticeView.findViewById(R.id.tvTitle);
        tvDisc = noticeView.findViewById(R.id.tvDisc);

        tvID.setText(id);
        tvTitle.setText(title);
        tvDisc.setText(description);

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
                dialog.setContentView(R.layout.provider_edit_notice_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                String clicked_id = tvID.getText().toString();
                String clicked_title = tvTitle.getText().toString();
                String clicked_description = tvDisc.getText().toString();

                TextView etTitle = dialog.findViewById(R.id.etTitle);
                TextView etDisc = dialog.findViewById(R.id.etDisc);

                etTitle.setText(clicked_title);
                etDisc.setText(clicked_description);

                Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("title", etTitle.getText().toString());
                        data.put("description", etDisc.getText().toString());

                        firebaseFirestore
                                .collection(firebaseAuth.getCurrentUser().getEmail() + "_notice")
                                .document(clicked_id)
                                .update(data);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        llData.addView(noticeView);
    }

    private void setupDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_new_notice_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText etTitle, etDisc;
        Button btnAdd;

        etTitle = dialog.findViewById(R.id.etTitle);
        etDisc = dialog.findViewById(R.id.etDisc);

        btnAdd = dialog.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id, description, title;

                id = createID();
                description = etDisc.getText().toString();
                title = etTitle.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("description", description);
                data.put("title", title);

                firebaseFirestore
                        .collection(firebaseAuth.getCurrentUser().getEmail() + "_notice")
                        .document(id)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Notice Data Added", Toast.LENGTH_SHORT).show();
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
        view = inflater.inflate(R.layout.provider_fragment_notice, container, false);

        init();

        return view;
    }
}
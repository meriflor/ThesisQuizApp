package com.trialProjects.test100.UserActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.R;

public class FragmentProfile extends Fragment {

    private TextView email, name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //ImageView profile = (ImageView) view.findViewById(R.id.profile_image);
        email = (TextView) view.findViewById(R.id.profile_email);
        name = (TextView) view.findViewById(R.id.profile_fullName);

        FirebaseAuth app_auth = FirebaseAuth.getInstance();
        FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
        String userID = app_auth.getCurrentUser().getUid();

        Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading);
        dialog.show();

        DocumentReference documentReference =  app_fireStore.collection("USERS").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        name.setText(documentSnapshot.getString("FULL_NAME"));
                        email.setText(documentSnapshot.getString("EMAIL"));
                        dialog.dismiss();
                    }else{
                        Log.d("LOGGER", "No such document");
                    }
                }else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
        return view;
    }


}
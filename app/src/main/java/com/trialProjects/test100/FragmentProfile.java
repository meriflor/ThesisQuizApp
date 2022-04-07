package com.trialProjects.test100;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfile extends Fragment {

    FirebaseAuth app_auth;
    String userID;
    TextView email, name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //ImageView profile = (ImageView) view.findViewById(R.id.profile_image);
        email = view.findViewById(R.id.profile_email);
        name = view.findViewById(R.id.profile_fullName);

        app_auth = FirebaseAuth.getInstance();
        DbQuery.app_fireStore = FirebaseFirestore.getInstance();
        userID = app_auth.getCurrentUser().getUid();

        //DocumentReference userDoc = DbQuery.app_fireStore.collection("USERS").document(userID);
        /*userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                email.setText(value.getString("EMAIL"));
                name.setText(value.getString("FULL_NAME"));
            }
        });*/
        DbQuery.app_fireStore.collection("USERS").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        email.setText(documentSnapshot.getString("EMAIL"));
                        name.setText(documentSnapshot.getString("FULL_NAME"));
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
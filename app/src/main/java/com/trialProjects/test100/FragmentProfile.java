package com.trialProjects.test100;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.xml.sax.DocumentHandler;

import java.util.Objects;

public class FragmentProfile extends Fragment {

    FirebaseAuth app_auth;
    String userID;
    TextView email, name;
    FirebaseFirestore app_fireStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //ImageView profile = (ImageView) view.findViewById(R.id.profile_image);
        email = (TextView) view.findViewById(R.id.profile_email);
        name = (TextView) view.findViewById(R.id.profile_fullName);

        app_auth = FirebaseAuth.getInstance();
        app_fireStore = FirebaseFirestore.getInstance();
        userID = app_auth.getCurrentUser().getUid();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading . . . ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        DocumentReference documentReference =  DbQuery.app_fireStore.collection("USERS").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        progressDialog.dismiss();
                        name.setText(documentSnapshot.getString("FULL_NAME"));
                        email.setText(documentSnapshot.getString("EMAIL"));
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
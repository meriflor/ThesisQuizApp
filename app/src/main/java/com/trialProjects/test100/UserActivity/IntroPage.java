package com.trialProjects.test100.UserActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.R;
import com.trialProjects.test100.activities.Registration;

public class IntroPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        FirebaseAuth app_auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if(app_auth.getCurrentUser() != null){
            DocumentReference userRef = firestore.collection("USERS")
                    .document(app_auth.getCurrentUser().getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        Intent intent = new Intent(IntroPage.this, Homepage.class);
                        intent.putExtra(Homepage.FULLNAME, doc.getString("FULL_NAME"));
                        intent.putExtra(Homepage.EMAIL, doc.getString("EMAIL"));
                        Log.d(TAG, doc.getString("FULL_NAME") + doc.getString("EMAIL"));
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else{
            //if not, straight to registration page

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(IntroPage.this, Registration.class));
                    finish();
                }
            }, 3000);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
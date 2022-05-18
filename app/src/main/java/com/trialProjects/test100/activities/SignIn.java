package com.trialProjects.test100.activities;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.R;
import com.trialProjects.test100.UserActivity.Homepage;

public class SignIn extends AppCompatActivity {

    private EditText et_email, et_pass;
    private Button btn_logIn;
    private TextView tv_register;
    private Dialog dialog;

    private String email, pass;
    private FirebaseAuth app_Auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_logIn = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

        app_Auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        //if user don't have an account yet
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
                finish();
            }
        });

        //signing In -- directed to Homepage
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(SignIn.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.loading);

                if(et_email.getText().toString().isEmpty()){
                    et_email.setError("Please enter your email.");
                    return;
                }if(et_pass.getText().toString().isEmpty()){
                    et_pass.setError("Please enter your password");
                    return;
                }else{
                    dialog.show();
                    logInUser();
                }
            }
        });
    }

    public void logInUser(){
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        //authentication
        app_Auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                getUserData(email, app_Auth.getCurrentUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void getUserData(String email, String uid) {
        DocumentReference userRef = firestore.collection("USERS").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    String fullName = doc.getString("FULL_NAME");
                    Intent intent = new Intent(SignIn.this, Homepage.class);
                    intent.putExtra(Homepage.FULLNAME, fullName);
                    intent.putExtra(Homepage.EMAIL, email);
                    startActivity(intent);
                    Log.d(TAG, fullName + email);
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }
}
package com.trialProjects.test100.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.DbQuery;
import com.trialProjects.test100.R;
import com.trialProjects.test100.Student_Homepage;
import com.trialProjects.test100.Teacher_Homepage;

public class SignIn extends AppCompatActivity {

    EditText et_email, et_pass;
    Button btn_logIn;
    TextView tv_register;

    String email, pass;
    FirebaseAuth app_Auth;
    FirebaseFirestore app_fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_logIn = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

        app_Auth = FirebaseAuth.getInstance();
        DbQuery.app_fireStore = FirebaseFirestore.getInstance();

        //if user don't have an account yet
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
                finish();
            }
        });

        //signing In
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                pass = et_pass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    et_email.setError("Please enter your email.");
                    return;
                }if(TextUtils.isEmpty(pass)){
                    et_pass.setError("Please enter your password");
                    return;
                }

                //authentication
                app_Auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                        checkUserType();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //to check if the user is a teacher or a student
    private void checkUserType() {
        FirebaseUser userID = app_Auth.getCurrentUser();
        DocumentReference userType = DbQuery.app_fireStore.collection("USERS").document(userID.getUid());
        userType.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if(documentSnapshot.getString("userType").equals("Teacher")) {
                    startActivity(new Intent(SignIn.this, Teacher_Homepage.class));
                    finish();
                }
                else if(documentSnapshot.getString("userType").equals("Student")) {
                    startActivity(new Intent(SignIn.this, Student_Homepage.class));
                    finish();
                }
            }
        });
    }
}
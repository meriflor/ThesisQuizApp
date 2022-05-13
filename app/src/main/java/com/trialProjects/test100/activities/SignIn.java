package com.trialProjects.test100.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.trialProjects.test100.UserActivity.Homepage;
import com.trialProjects.test100.R;

public class SignIn extends AppCompatActivity {

    EditText et_email, et_pass;
    Button btn_logIn;
    TextView tv_register;

    String email, pass;
    FirebaseAuth app_Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_logIn = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

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

                if(et_email.getText().toString().isEmpty()){
                    et_email.setError("Please enter your email.");
                    return;
                }if(et_pass.getText().toString().isEmpty()){
                    et_pass.setError("Please enter your password");
                    return;
                }else{
                    logInUser();
                }
            }
        });
    }

    public void logInUser(){
        app_Auth = FirebaseAuth.getInstance();
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        //authentication
        app_Auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                //checkUserType();
                startActivity(new Intent(getApplicationContext(), Homepage.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
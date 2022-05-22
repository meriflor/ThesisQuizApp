package com.trialProjects.test100.activities;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.R;
import com.trialProjects.test100.UserActivity.Homepage;

public class Registration extends AppCompatActivity {

    private TextView tv_logIn;
    private Button btn_register;
    private EditText et_email, et_fullName, et_pass, et_rePass, et_schoolId;
    private Spinner spinner;
    private FirebaseAuth app_auth;
    private FirebaseFirestore app_fireStore;

    String userType, email, pass, fullName, rePass, schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        tv_logIn = findViewById(R.id.tv_login);
        btn_register = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_fullName = findViewById(R.id.et_fullName);
        et_pass = findViewById(R.id.et_pass);
        et_rePass = findViewById(R.id.et_rePass);
        et_schoolId = findViewById(R.id.et_schoolId);
        spinner = findViewById(R.id.spinner);

        app_auth = FirebaseAuth.getInstance();
        app_fireStore = FirebaseFirestore.getInstance();

        //for Spinner-- list
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //for already have an account - directed to login Page
        tv_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });

        //for register
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = spinner.getSelectedItem().toString();
                email = et_email.getText().toString().trim();
                fullName = et_fullName.getText().toString().trim();
                pass = et_pass.getText().toString().trim();
                schoolId = et_schoolId.getText().toString().trim();
                rePass = et_rePass.getText().toString().trim();

                Dialog dialog = new Dialog(Registration.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.loading);

                //conditions not met
                if(fullName.isEmpty()){
                    et_fullName.setError("Please enter your name");
                }else if(TextUtils.isEmpty(email) || !email.contains("@")){
                    et_email.setError("Please enter your Email");
                }else if(TextUtils.isEmpty(pass)) {
                    et_pass.setError("Please enter your Password");
                }else if(pass.length() < 8) {
                    et_pass.setError("Password must be 8 character");
                }else if(rePass.isEmpty()||!rePass.equals(pass)) {
                    et_rePass.setError("Password don't match");
                }else if(userType.equals("Select User Type")){
                    showToast("Choose user type");
                }else{
                    dialog.show();
                    //if whatever user selected then proceed to store user data to different user type.
                    if(userType.equals("Student")){
                        app_auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    showToast("Registered Successfully");
                                    //create sa student user
                                    DbQuery.createStudentData(email, fullName, schoolId, new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            intentPutExtra(email, fullName);
                                            dialog.dismiss();
                                        }
                                        @Override
                                        public void onFailure() {
                                            showToast("Something went wrong!");
                                            dialog.dismiss();
                                        }
                                    });
                                }else{
                                    showToast("Error!");
                                    dialog.dismiss();
                                }
                            }
                        });
                    }else if(userType.equals("Teacher")){
                        app_auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    showToast("Registered Successfully");
                                    //create sa teacher user
                                    DbQuery.createTeacherData(email, fullName, schoolId, new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            intentPutExtra(email, fullName);
                                            dialog.dismiss();
                                        }
                                        @Override
                                        public void onFailure() {
                                            showToast("Something went wrong!");
                                            dialog.dismiss();
                                        }
                                    });
                                }else{
                                    showToast("Error!");
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(Registration.this, text,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void intentPutExtra(String name, String email) {
        Intent intent = new Intent(Registration.this, Homepage.class);
        intent.putExtra(Homepage.FULLNAME, name);
        intent.putExtra(Homepage.EMAIL, email);
        Log.d(TAG, fullName + email);
        startActivity(intent);
        finish();
    }
}
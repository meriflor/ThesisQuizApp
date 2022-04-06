package com.trialProjects.test100.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.DbQuery;
import com.trialProjects.test100.MyCompleteListener;
import com.trialProjects.test100.R;
import com.trialProjects.test100.Student_Homepage;
import com.trialProjects.test100.Teacher_Homepage;

public class Registration extends AppCompatActivity {

    TextView tv_logIn;
    Button btn_register;
    EditText et_email, et_fullName, et_pass, et_rePass, et_schoolId;
    Spinner spinner;
    FirebaseAuth app_auth;

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
        DbQuery.app_fireStore = FirebaseFirestore.getInstance();

        //if there is a logged in user, it will be directed to its homepage
        if(app_auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Student_Homepage.class));
            finish();
        }

        //for Spinner-- list
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //for already have an account - directed to login Page
        tv_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
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

                //conditions not met
                if(TextUtils.isEmpty(email) || !email.contains("@")){
                    et_email.setError("Please enter your Email");
                }else if(TextUtils.isEmpty(pass)){
                    et_pass.setError("Please enter your Password");
                }else if(pass.length() < 8){
                    et_pass.setError("Password must be 8 character");
                }else if(rePass.isEmpty()||!rePass.equals(pass)) {
                    et_rePass.setError("Password don't match");
                }else{
                    //if whatever user selected then proceed to store user data to different user type.
                    if(userType.equals("Student")){
                        app_auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    //create sa student user
                                    DbQuery.createStudentData(email, fullName, schoolId, new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            startActivity(new Intent(Registration.this, Student_Homepage.class));
                                            finish();
                                        }
                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(Registration.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(Registration.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if(userType.equals("Teacher")){
                        app_auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    //create sa teacher user
                                    DbQuery.createTeacherData(email, fullName, schoolId, new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            startActivity(new Intent(Registration.this, Teacher_Homepage.class));
                                            finish();
                                        }
                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(Registration.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(Registration.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }



}
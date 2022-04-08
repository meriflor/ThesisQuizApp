package com.trialProjects.test100;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore app_fireStore;


    public static void createStudentData(String email, String fullName, String schoolId, MyCompleteListener completeListener){

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = app_fireStore.collection("USERS").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL", email);
        userData.put("FULL_NAME", fullName);
        userData.put("SCHOOL_ID", schoolId);
        userData.put("userType", "Student");

        userDoc.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();
            }
        });
    }

    public static void createTeacherData(String email, String fullName, String schoolId, MyCompleteListener completeListener){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = app_fireStore.collection("USERS").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL", email);
        userData.put("FULL_NAME", fullName);
        userData.put("SCHOOL_ID", schoolId);
        userData.put("userType", "Teacher");

        userDoc.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();
            }
        });
    }

    public static void createClass(String name, String subject, String section, String teacher){

        DocumentReference classDoc = app_fireStore.collection("CLASS").document();
        Map<String, Object> classData = new HashMap<>();
        classData.put("class_name", name);
        classData.put("class_subject", subject);
        classData.put("class_section", section);
        classData.put("class_teacher", teacher);

        classDoc.set(classData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

}

package com.trialProjects.test100;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.activities.JoinClasses;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore app_fireStore;
    DocumentSnapshot lastAddedClass;


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

    public static void createClass(String name, String section, MyCompleteListener completeListener) {

        String teacherID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference classRef = app_fireStore
                .collection("CLASSES")
                .document();

        AddClasses classes = new AddClasses();
        classes.setClassName(name);
        classes.setClassID(classRef.getId());
        classes.setTeacherID(teacherID);
        classes.setClassSection(section);
        classes.setAccessCode(classRef.getId());

        classRef.set(classes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    completeListener.onSuccess();
                }else{
                    completeListener.onFailure();
                }
            }
        });
    }

    public static void joinClass(String accessCode, MyCompleteListener completeListener) {

        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference classRef = app_fireStore
                .collection("Students")
                .document();

        JoinClasses classes = new JoinClasses();
        classes.setStudentID(studentID);
        //classes.setAccessCode(accessCode);

        classRef.set(classes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    completeListener.onSuccess();
                }else{
                    completeListener.onFailure();
                }
            }
        });
    }
}

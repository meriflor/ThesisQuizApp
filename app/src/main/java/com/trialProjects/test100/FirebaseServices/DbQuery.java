package com.trialProjects.test100.FirebaseServices;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.Student.JoinClasses;
import com.trialProjects.test100.Teacher.AddClasses;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();

    public static void createQuestionName(String questionName,String quizIDD, String optionA, String optionB,
                                          String optionC, String optionD, String answer, MyCompleteListener completeListener){
        DocumentReference questionList = app_fireStore.collection("QUESTIONS").document();
        Map<String, Object> questionNameData = new HashMap<>();
        questionNameData.put("question",questionName);
        questionNameData.put("optionA",optionA);
        questionNameData.put("optionB",optionB);
        questionNameData.put("optionC",optionC);
        questionNameData.put("optionD",optionD);
        questionNameData.put("answer",answer);
        questionNameData.put("quizId",quizIDD);
        questionNameData.put("questionId",questionList.getId());
        questionList.set(questionNameData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) { completeListener.onSuccess(); }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { completeListener.onFailure(); }
        });
    }

    public static void createQuizName(String quizname, String classid, String teacherID, String className, MyCompleteListener completeListener){
        DocumentReference quizList = app_fireStore.collection("QUIZLIST").document();
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("quizName",quizname);
        quizData.put("classId",classid);
        quizData.put("quizId",quizList.getId());
        quizData.put("teacherID", teacherID);
        quizData.put("className", className);
        quizData.put("visibility", false);
        Log.d(TAG, quizList.getId());
        quizList.set(quizData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                completeListener.onSuccess();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { completeListener.onFailure(); }
        });


        CollectionReference studRef = FirebaseFirestore.getInstance().collection("STUDENTS");
        Query query = studRef.whereEqualTo("classID", classid);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot doc: querySnapshot){
                        String studentID = doc.getString("studentID");

                        DocumentReference studQuizRef = FirebaseFirestore.getInstance().collection("STUDENT_QUIZ")
                                .document();
                        Map<String, Object> studQuizData = new HashMap<>();
                        studQuizData.put("classID", classid);
                        studQuizData.put("studentID", studentID);
                        studQuizData.put("teacherID", teacherID);
                        studQuizData.put("quizID", quizList.getId());
                        studQuizData.put("quizName", quizname);
                        studQuizData.put("attempt", false);
                        studQuizData.put("score", "0");
                        studQuizData.put("visibility", false);

                        studQuizRef.set(studQuizData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "Successfully loaded the " + quizname + " file");
                                }else{
                                    Log.d(TAG, "Error Occurred");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

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

    public static void createClass(String name, String section, String teacherName, MyCompleteListener completeListener) {
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
        classes.setTeacherName(teacherName);

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

    public static void checkClassExist(String classID, MyCompleteListener completeListener){
        app_fireStore = FirebaseFirestore.getInstance();
        DocumentReference classRef = app_fireStore.collection("CLASSES").document(classID);
        classRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    completeListener.onSuccess();
                }else{
                    completeListener.onFailure();
                }
            }
        });
    }

    public static void checkStudentClassExist(String classID, MyCompleteListener completeListener){
        app_fireStore = FirebaseFirestore.getInstance();
        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference classRef = app_fireStore.collection("STUDENTS");
        Query classQuery = classRef
                .whereEqualTo("studentID", studentID);

        classQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getString("studentID"));
                        if(document.getString("classID").equals(classID)){
                            completeListener.onSuccess();
                            return;
                        }
                    }
                    completeListener.onFailure();
                }
            }
        });
    }


    public static void joinClass(String classID, String studentID, String studentName, MyCompleteListener completeListener){

        DocumentReference classesRef = FirebaseFirestore.getInstance()
                .collection("CLASSES")
                .document(classID);
        classesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String className = documentSnapshot.getString("className");
                String classSection = documentSnapshot.getString("classSection");

                CollectionReference classRef = app_fireStore.collection("STUDENTS");

                classRef.add(new JoinClasses(classID, studentID, studentName, className, classSection));
                completeListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completeListener.onFailure();
            }
        });

    }

    public static void joinUpdateQuiz(String classID, String studentID, MyCompleteListener completeListener) {
        CollectionReference ref = app_fireStore.collection("QUIZLIST");
        Query query = ref.whereEqualTo("classId", classID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot doc: querySnapshot){
                        String quizName = doc.getString("quizName");
                        String quizID = doc.getString("quizId");
                        String teacherID = doc.getString("teacherID");
                        DocumentReference studQuizRef = app_fireStore.collection("STUDENT_QUIZ")
                                .document();
                        Map<String, Object> studQuizData = new HashMap<>();
                        studQuizData.put("classID", classID);
                        studQuizData.put("studentID", studentID);
                        studQuizData.put("teacherID", teacherID);
                        studQuizData.put("quizID", quizID);
                        studQuizData.put("quizName", quizName);
                        studQuizData.put("attempt", false);
                        studQuizData.put("score", "0");
                        studQuizData.put("visibility", false);

                        studQuizRef.set(studQuizData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "Successfully loaded the " + quizName + " file");
                                }else{
                                    Log.d(TAG, "Error Occurred");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
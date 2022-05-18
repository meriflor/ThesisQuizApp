package com.trialProjects.test100.Student;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.UserActivity.Homepage;
import com.trialProjects.test100.R;

import java.util.HashMap;
import java.util.Map;

public class StudentResultActivity extends AppCompatActivity {

    public static final String SCORE = "score";
    public static final String SCORE_OVER = "scoreOver";
    public static final String STUDENTNAME = "student Name";
    public static final String QUIZID = "Quiz ID";
    public static final String STUDENTID = "studID";
    public static final String STUDENTQUIZID = "studquizID";
    private TextView studentName, studentScore;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        String score, scoreOver, student, quizID, studentID, studentQuizID;
        Intent intent = getIntent();
        score = intent.getStringExtra(SCORE);
        scoreOver = intent.getStringExtra(SCORE_OVER);
        student = intent.getStringExtra(STUDENTNAME);
        quizID = intent.getStringExtra(QUIZID);
        studentID = intent.getStringExtra(STUDENTID);
        studentQuizID = intent.getStringExtra(STUDENTQUIZID);

        studentName = findViewById(R.id.result_studentName);
        studentScore = findViewById(R.id.result_studentScore);
        btnDone = findViewById(R.id.result_btnDone);

        studentName.setText(student);
        studentScore.setText(String.valueOf(score) + "/" + String.valueOf(scoreOver));

        updateStudentQuiz(String.valueOf(score), studentQuizID);
//
//        DbQuery.storeStudentRecord(quizID, score, student, studentID, new MyCompleteListener() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "Successful");
//            }
//            @Override
//            public void onFailure() {
//                Log.d(TAG, "failed");
//            }
//        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentResultActivity.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void updateStudentQuiz(String score, String studentQuizID) {
        DocumentReference studRef = FirebaseFirestore.getInstance()
                .collection("STUDENT_QUIZ")
                .document(studentQuizID);
        Map<String, Object> data = new HashMap<>();
        data.put("score", score);
        Log.d(TAG, score + " " + studentQuizID);
        studRef.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Quiz taken with "+ score +" score");
                }
                else{
                    Log.d(TAG, "Error Occurred");
                }
            }
        });
    }
}
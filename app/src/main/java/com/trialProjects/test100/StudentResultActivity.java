package com.trialProjects.test100;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentResultActivity extends AppCompatActivity {

    public static final String SCORE = "score";
    public static final String SCORE_OVER = "scoreOver";
    private TextView studentName, studentScore;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        String score, scoreOver;
        Intent intent = getIntent();
        score = intent.getStringExtra(SCORE);
        scoreOver = intent.getStringExtra(SCORE_OVER);



        studentName = findViewById(R.id.result_studentName);
        studentScore = findViewById(R.id.result_studentScore);
        btnDone = findViewById(R.id.result_btnDone);

        studentScore.setText(String.valueOf(score) + "/" + String.valueOf(scoreOver));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentResultActivity.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
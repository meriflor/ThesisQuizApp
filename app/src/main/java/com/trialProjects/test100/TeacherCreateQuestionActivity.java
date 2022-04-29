package com.trialProjects.test100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class TeacherCreateQuestionActivity extends AppCompatActivity {
    public static final String QUIZID = "hala";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_question);
        String quizid;
        Intent intent = getIntent();
        quizid = intent.getStringExtra(QUIZID);
    }
}
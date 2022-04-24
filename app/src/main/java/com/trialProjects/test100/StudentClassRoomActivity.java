package com.trialProjects.test100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class StudentClassRoomActivity extends AppCompatActivity {
    public static final String CLASSNAME ="Class Name";
    public static final String CLASSROOMID = "Class Room Id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_room);
        String classNAME;
        String classID;
        TextView et_ClassName;
        TextView et_ClassId;
        et_ClassName = findViewById(R.id.et_ClassNAME);
        et_ClassId = findViewById(R.id.et_ClassID);

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);

        et_ClassName.setText("Class Name: "+classNAME);
        et_ClassId.setText("Class Id: "+ classID);
    }
}
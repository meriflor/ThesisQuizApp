package com.trialProjects.test100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class TeacherClassRoomActivity extends AppCompatActivity {
public static final String CLASSNAME ="Class Name";
public static final String CLASSROOMID = "Class Room Id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_room);

        String classNAME;
        String classID;
        TextView etClassName;
        TextView etClassId;
        etClassName = findViewById(R.id.etClassNAME);
        etClassId = findViewById(R.id.etClassID);

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);

        etClassName.setText("Class Name: "+classNAME);
        etClassId.setText("Class Id: "+ classID);


        FloatingActionButton createQuizBtn = findViewById(R.id.createQuizBtn);
        createQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showText("This is create quiz button");

            }
        });
        FloatingActionButton createAssignmentBtn = findViewById(R.id.createAssignmentBtn);
        createAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showText("This is create assignment button");
            }
        });
    }

    public void showText(String name){
        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
    }
}
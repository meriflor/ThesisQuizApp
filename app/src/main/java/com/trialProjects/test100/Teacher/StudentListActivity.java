package com.trialProjects.test100.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.trialProjects.test100.R;
import com.trialProjects.test100.Student.StudentClassRoomActivity;
import com.trialProjects.test100.Student.StudentQuizAdapter;
import com.trialProjects.test100.Student.StudentQuizModel;

import java.util.Collection;

public class StudentListActivity extends AppCompatActivity {

    public static final String CLASSID = "classID";
    public static final String CLASSNAME = "className";
    private  StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        String classID, classNAME;
        Intent intent = getIntent();
        classID = intent.getStringExtra(CLASSID);
        classNAME = intent.getStringExtra(CLASSNAME);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //added code
        Toolbar toolbar = findViewById(R.id.teacher_studentListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of code

        CollectionReference studRef = firestore.collection("STUDENTS");
        Query studQuery = studRef
                .whereEqualTo("classID", classID)
                .orderBy("studentName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<StudentListModel> options = new FirestoreRecyclerOptions.Builder<StudentListModel>()
                .setQuery(studQuery, StudentListModel.class)
                .build();
        adapter = new StudentListAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.teacher_recStudentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
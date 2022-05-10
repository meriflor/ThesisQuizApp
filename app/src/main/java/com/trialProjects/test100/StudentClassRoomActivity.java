package com.trialProjects.test100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentClassRoomActivity extends AppCompatActivity {
    public static final String CLASSNAME ="Class Name";
    public static final String CLASSROOMID = "Class Room Id";
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private StudentQuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_room);
        String classNAME;
        String classID;

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);

        //added code
        Toolbar toolbar = findViewById(R.id.student_classroomToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of code

        app_fireStore = FirebaseFirestore.getInstance();
        CollectionReference quizRef = app_fireStore.collection("QUIZLIST");
        Query quizQuery = quizRef
                .whereEqualTo("classId", classID)
                .orderBy("quizId", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<StudentQuizModel> options = new FirestoreRecyclerOptions.Builder<StudentQuizModel>()
                .setQuery(quizQuery, StudentQuizModel.class)
                .build();

        adapter = new StudentQuizAdapter(options);
        RecyclerView studentQuizRecyclerView = findViewById(R.id.studentQuizRecyclerView);
        studentQuizRecyclerView.setHasFixedSize(true);
        studentQuizRecyclerView.setLayoutManager(new LinearLayoutManager(StudentClassRoomActivity.this));
        studentQuizRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new StudentQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                StudentQuizModel studentQuizModel = documentSnapshot.toObject(StudentQuizModel.class);
                String quizID = documentSnapshot.getString("quizId");
                String quizName = documentSnapshot.getString("quizName");
                Intent intent = new Intent(StudentClassRoomActivity.this, StudentQuestionsActivity.class);
                intent.putExtra(StudentQuestionsActivity.QUIZID, quizID);
                intent.putExtra(StudentQuestionsActivity.QUIZNAME, quizName);
                startActivity(intent);
            }
        });

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
package com.trialProjects.test100.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.trialProjects.test100.R;

public class StudentScoreActivity extends AppCompatActivity {

    public static final String CLASSNAME = "ClassName";
    public static final String CLASSID = "ClassID";
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private TeacherQuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_score);

        String classNAME, classID;

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSID);

        //added code
        Toolbar toolbar = findViewById(R.id.teacher_studentScoreToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of code


        CollectionReference quizRef = app_fireStore.collection("QUIZLIST");
        Query quizQuery = quizRef
                .whereEqualTo("classId", classID)
                .orderBy("quizName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<QuizModel> options = new FirestoreRecyclerOptions.Builder<QuizModel>()
                .setQuery(quizQuery, QuizModel.class)
                .build();
        adapter = new TeacherQuizAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.teacher_recStudentScore);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentScoreActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeacherQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                QuizModel quizModel = documentSnapshot.toObject(QuizModel.class);
                String quizid = documentSnapshot.getString("quizId");
                String quizName = documentSnapshot.getString("quizName");
                Intent intent = new Intent (StudentScoreActivity.this, ScoreActivity.class);
                intent.putExtra(ScoreActivity.QUIZID,quizid);
                intent.putExtra(ScoreActivity.QUIZNAME,quizName);
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
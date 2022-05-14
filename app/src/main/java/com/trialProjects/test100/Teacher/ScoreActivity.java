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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.trialProjects.test100.R;

public class ScoreActivity extends AppCompatActivity {


    public static final String QUIZID = "quizID";
    public static final String QUIZNAME = "quizname";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        String quizID, quizName;
        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        quizName = intent.getStringExtra(QUIZNAME);

        //added code
        Toolbar toolbar = findViewById(R.id.teacher_studentScoreToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(quizName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of code

        CollectionReference scoreRef = firestore.collection("STUDENT_SCORE");
        Query scoreQuery = scoreRef
                .whereEqualTo("quizID", quizID)
                .orderBy("score", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ScoreModel> options = new FirestoreRecyclerOptions.Builder<ScoreModel>()
                .setQuery(scoreQuery, ScoreModel.class)
                .build();
        adapter = new ScoreAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.teacher_recStudentScoreList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreActivity.this));
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
package com.trialProjects.test100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentQuestionsActivity extends AppCompatActivity {

    public static String QUIZID = "Quiz";
    private StudentQuestionsAdapter adapter;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);

        String quizID;
        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        TextView tv_quizID = findViewById(R.id.tv_quizID);
        tv_quizID.setText("Quiz ID: " + quizID);

        CollectionReference quesRef = firestore.collection("QUESTIONS");
        Query query = quesRef
                .whereEqualTo("quizId", quizID)
                .orderBy("questionId", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<StudentQuestionsModel> options = new FirestoreRecyclerOptions.Builder<StudentQuestionsModel>()
                .setQuery(query, StudentQuestionsModel.class)
                .build();
        adapter = new StudentQuestionsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.student_QuestionRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentQuestionsActivity.this));
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

}
package com.trialProjects.test100.Student;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.R;

public class StudentClassRoomActivity extends AppCompatActivity {
    public static final String CLASSNAME ="Class Name";
    public static final String CLASSROOMID = "Class Room Id";
    public static final String STUDENTNAME = "Student name";
    public static final String STUDENTID = "studID";
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private StudentQuizAdapter adapter;
    private String studentName, studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_room);
        String classNAME;
        String classID;

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);
        studentName = intent.getStringExtra(STUDENTNAME);
        studentID = intent.getStringExtra(STUDENTID);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.student_classroomToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app_fireStore = FirebaseFirestore.getInstance();

        CollectionReference quizRef = app_fireStore.collection("STUDENT_QUIZ");
        Query quizQuery = quizRef
                .whereEqualTo("studentID", studentID)
                .whereEqualTo("attempt", false)
                .whereEqualTo("classID", classID)
                .orderBy("quizName", Query.Direction.ASCENDING);

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
                String quizID = documentSnapshot.getString("quizID");
                String quizName = documentSnapshot.getString("quizName");
                String studentQuizID = documentSnapshot.getId();

                Log.d(TAG, quizID + " what is this?" + quizName);

                CollectionReference ref = app_fireStore.collection("QUESTIONS");
                ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();
                            for (QueryDocumentSnapshot doc: querySnapshot){
                                if(doc.getString("quizId").equals(quizID)){
                                    Log.d(TAG, doc.getString("quizId") + " " + quizID);
                                    Intent intent = new Intent(StudentClassRoomActivity.this, StudentQuestionsActivity.class);
                                    intent.putExtra(StudentQuestionsActivity.QUIZID, quizID);
                                    intent.putExtra(StudentQuestionsActivity.QUIZNAME, quizName);
                                    intent.putExtra(StudentQuestionsActivity.STUDENTNAME, studentName);
                                    intent.putExtra(StudentQuestionsActivity.STUDENTID, studentID);
                                    intent.putExtra(StudentQuestionsActivity.STUDENTQUIZID, studentQuizID);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            Toast.makeText(StudentClassRoomActivity.this,
                                    "You are not allowed to take this quiz yet.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
package com.trialProjects.test100.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.R;
import com.trialProjects.test100.UserActivity.Homepage;
import com.trialProjects.test100.activities.Registration;

import java.util.List;

public class TeacherCreateQuestionActivity extends AppCompatActivity {
    public static final String QUIZNAME = "quizNAME";
    public static final String QUIZID = "quizID";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private CreateQuestionAdapter adapter;
    private RecyclerView recyclerView;
    private Button quizDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_question);

        String quizName;
        String quizID;
        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        quizName = intent.getStringExtra(QUIZNAME);
        Button btnAddQuestion;

        //added code
        Toolbar toolbar = findViewById(R.id.teacher_quizToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(quizName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of code

        quizDelete = findViewById(R.id.btn_delete_quiz);

        quizDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TeacherCreateQuestionActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("You are about to delete the Quiz.");
                dialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("QUIZLIST")
                                        .document(quizID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(TeacherCreateQuestionActivity.this,
                                                    "Quiz Deleted", Toast.LENGTH_SHORT).show();
                                            FirebaseFirestore.getInstance().collection("QUESTIONS")
                                                    .whereEqualTo("quizId", quizID)
                                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    WriteBatch batch = FirebaseFirestore.getInstance().batch();
                                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                                    for(DocumentSnapshot snapshot: snapshotList){
                                                        batch.delete(snapshot.getReference());
                                                    }
                                                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d(TAG, "Questions were deleted");

                                                            FirebaseFirestore.getInstance().collection("STUDENT_QUIZ")
                                                                    .whereEqualTo("quizID", quizID)
                                                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    WriteBatch batch = FirebaseFirestore.getInstance().batch();
                                                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                                                    for(DocumentSnapshot snapshot: snapshotList){
                                                                        batch.delete(snapshot.getReference());
                                                                    }
                                                                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Log.d(TAG, "Quizzes assigned for Students were deleted");
                                                                            startActivity(new Intent(TeacherCreateQuestionActivity.this,
                                                                                    Homepage.class));
                                                                            finish();
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "Error");
                                                        }
                                                    });
                                                }
                                            });
                                        }else{
                                            Log.d(TAG, "Deleting Canceled");
                                        }
                                    }
                                });
                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        CollectionReference questionNameRef = app_fireStore.collection("QUESTIONS");
        Query questionNameQuery = questionNameRef
                .whereEqualTo("quizId",quizID )
                .orderBy("question", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<QuestionModel> options = new FirestoreRecyclerOptions.Builder<QuestionModel>().setQuery(questionNameQuery, QuestionModel.class).build();
        adapter = new CreateQuestionAdapter(options);

        recyclerView = findViewById(R.id.create_question_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherCreateQuestionActivity.this));
        recyclerView.setAdapter(adapter);

        btnAddQuestion = findViewById(R.id.btn_add_question);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(TeacherCreateQuestionActivity.this);
                View createQuestionName = getLayoutInflater().inflate(R.layout.popup_window_create_question, null);
                EditText questionName, etOptionA, etOptionB, etOptionC, etOptionD, etAnswer;
                Button btn_create_question, btn_cancel;

                questionName = createQuestionName.findViewById(R.id.et_createQuestionName);
                etOptionA = createQuestionName.findViewById(R.id.et_optionA);
                etOptionB = createQuestionName.findViewById(R.id.et_optionB);
                etOptionC = createQuestionName.findViewById(R.id.et_optionC);
                etOptionD = createQuestionName.findViewById(R.id.et_optionD);
                etAnswer = createQuestionName.findViewById(R.id.et_answer);
                btn_create_question = createQuestionName.findViewById(R.id.btn_create_question_name);
                btn_cancel = createQuestionName.findViewById(R.id.btnCancelCreateQuestion);

                dialogBuilder.setView(createQuestionName);
                dialog = dialogBuilder.create();
                dialog.show();

                btn_create_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String questionname = questionName.getText().toString().trim();
                        String optionA = etOptionA.getText().toString().trim();
                        String optionB = etOptionB.getText().toString().trim();
                        String optionC = etOptionC.getText().toString().trim();
                        String optionD = etOptionD.getText().toString().trim();
                        String answer = etAnswer.getText().toString().trim();
                        if(questionname.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty()) {
                            Toast.makeText(TeacherCreateQuestionActivity.this,"Please fill all the missing fields",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            DbQuery.createQuestionName(questionname, quizID, optionA, optionB, optionC, optionD, answer, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(TeacherCreateQuestionActivity.this,"Sucessfully Created.",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(TeacherCreateQuestionActivity.this,"Failed to Create.",Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    }
                }
                );
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
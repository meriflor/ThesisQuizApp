package com.trialProjects.test100;

import static com.trialProjects.test100.DbQuery.app_fireStore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TeacherCreateQuestionActivity extends AppCompatActivity {
    public static final String QUIZID = "hala";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private CreateQuestionAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_question);

        String quizID;
        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        Button btnAddQuestion;

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
                EditText questionName;
                Button btn_create_question, btn_cancel;
                questionName = createQuestionName.findViewById(R.id.et_createQuestionName);
                btn_create_question = createQuestionName.findViewById(R.id.btn_create_question_name);
                btn_cancel = createQuestionName.findViewById(R.id.btnCancelCreateQuestion);
                dialogBuilder.setView(createQuestionName);
                dialog = dialogBuilder.create();
                dialog.show();

                btn_create_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            String questionname = questionName.getText().toString().trim();
                        if(questionname.isEmpty())
                        {
                            Toast.makeText(TeacherCreateQuestionActivity.this,"Please enter the class name and section",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            DbQuery.createQuestionName(questionname,quizID, new MyCompleteListener() {
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
}
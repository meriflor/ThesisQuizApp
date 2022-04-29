package com.trialProjects.test100;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TeacherClassRoomActivity extends AppCompatActivity {
public static final String CLASSNAME ="Class Name";
public static final String CLASSROOMID = "Class Room Id";
private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
private TeacherQuizAdapter adapter;
private AlertDialog.Builder dialogBuilder;
private AlertDialog dialog;

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


        app_fireStore = FirebaseFirestore.getInstance();
        CollectionReference quizRef = app_fireStore.collection("QUIZLIST");
        Query quizQuery = quizRef
                    .whereEqualTo("classId", classID)
               //  .whereEqualTo("classId",classID )
                .orderBy("quizName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<QuizModel> options = new FirestoreRecyclerOptions.Builder<QuizModel>().setQuery(quizQuery, QuizModel.class).build();
       adapter = new TeacherQuizAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.teacher_quiz_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherClassRoomActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeacherQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                QuizModel quizModel = documentSnapshot.toObject(QuizModel.class);
                String quizid = documentSnapshot.getId().toString();
                Intent intent = new Intent (TeacherClassRoomActivity.this,TeacherCreateQuestionActivity.class);
                intent.putExtra(TeacherCreateQuestionActivity.QUIZID,quizid);
                startActivity(intent);
            }
        });

        FloatingActionButton createQuizBtn = findViewById(R.id.createQuizBtn);
        createQuizBtn.setOnClickListener(new View.OnClickListener() {
            EditText etCreateQuiz;
            Button btnCreateQuiz;
            @Override
            public void onClick(View view) {
                showText("This is create quiz button");
                    dialogBuilder = new AlertDialog.Builder(TeacherClassRoomActivity.this);
                    View createQuizView = getLayoutInflater().inflate(R.layout.pop_up_window_create_quiz, null);

                    etCreateQuiz= createQuizView.findViewById(R.id.et_create_quiz);

                    btnCreateQuiz= createQuizView.findViewById(R.id.btn_create_quiz);
                    Button btnCancelQuiz;
                    btnCancelQuiz= createQuizView.findViewById(R.id.btn_cancel_quiz);
                    dialogBuilder.setView(createQuizView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String quizName = etCreateQuiz.getText().toString().trim();
                            if(quizName.isEmpty()){
                                Toast.makeText(TeacherClassRoomActivity.this, "Please enter Quiz name or title",Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                    addNewQuiz(quizName,classID);
                                    dialog.dismiss();
                            }
                        }
                    });

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
    public void addNewQuiz(String quizName, String classID){
        DbQuery.createQuizName(quizName, classID, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(TeacherClassRoomActivity.this,"Created Sucessfully!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(TeacherClassRoomActivity.this,"Create Failed",Toast.LENGTH_SHORT);
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

    public void showText(String name){
        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
    }
}
package com.trialProjects.test100.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.R;

public class TeacherClassRoomActivity extends AppCompatActivity {

    public static final String CLASSNAME ="Class Name";
    public static final String CLASSROOMID = "Class Room Id";
    public static final String ACCESSCODE = "accessCode";
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private TeacherQuizAdapter adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tv_accessCode;
    private ImageView clipBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_room);

        String classNAME;
        String classID, accessCode;

        Intent intent = getIntent();
        classNAME = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);
        accessCode = intent.getStringExtra(ACCESSCODE);

        tv_accessCode = findViewById(R.id.tv_accessCode);
        clipBoard = findViewById(R.id.img_copy);

        //
        tv_accessCode.setText(accessCode);
        clipBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", tv_accessCode.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                clipBoard.setImageDrawable(getResources().getDrawable(R.drawable.icon_copied));
                Toast.makeText(TeacherClassRoomActivity.this, "Text copied to clipboard",
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Toolbar
        Toolbar toolbar = findViewById(R.id.student_classroomToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollectionReference quizRef = app_fireStore.collection("QUIZLIST");
        Query quizQuery = quizRef
                .whereEqualTo("classId", classID)
                .orderBy("quizName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<QuizModel> options = new FirestoreRecyclerOptions.Builder<QuizModel>()
                .setQuery(quizQuery, QuizModel.class)
                .build();
        adapter = new TeacherQuizAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.teacher_quiz_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherClassRoomActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeacherQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                QuizModel quizModel = documentSnapshot.toObject(QuizModel.class);
                String quizid = documentSnapshot.getString("quizId");
                String quizName = documentSnapshot.getString("quizName");
                Intent intent = new Intent (TeacherClassRoomActivity.this,TeacherCreateQuestionActivity.class);
                intent.putExtra(TeacherCreateQuestionActivity.QUIZNAME,quizName);
                intent.putExtra(TeacherCreateQuestionActivity.QUIZID,quizid);
                startActivity(intent);
            }
        });

        FloatingActionButton createQuizBtn = findViewById(R.id.createQuizBtn);
        FloatingActionButton btnStudentList = findViewById(R.id.student_List);
        FloatingActionButton btnScore = findViewById(R.id.Score);

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherClassRoomActivity.this, StudentScoreActivity.class);
                intent.putExtra(StudentScoreActivity.CLASSNAME, classNAME);
                intent.putExtra(StudentScoreActivity.CLASSID, classID);
                startActivity(intent);
            }
        });

        btnStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherClassRoomActivity.this, StudentListActivity.class);
                intent.putExtra(StudentListActivity.CLASSID, classID);
                Log.d(TAG, classID);
                intent.putExtra(StudentListActivity.CLASSNAME, classNAME);
                startActivity(intent);
            }
        });

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
                    btnCancelQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
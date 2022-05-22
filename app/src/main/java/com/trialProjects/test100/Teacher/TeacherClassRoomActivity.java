package com.trialProjects.test100.Teacher;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.R;

import java.util.List;

public class TeacherClassRoomActivity extends AppCompatActivity {

    public static final String CLASSNAME ="Class Name";
    public static final String CLASSROOMID = "Class Room Id";
    public static final String ACCESSCODE = "accessCode";
    public static final String TEACHERID = "teacherID";
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private TeacherQuizAdapter adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tv_accessCode;
    private ImageView clipBoard, classDelete;
    private String teacherID, className, classID, accessCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_room);

        Intent intent = getIntent();
        className = intent.getStringExtra(CLASSNAME);
        classID = intent.getStringExtra(CLASSROOMID);
        accessCode = intent.getStringExtra(ACCESSCODE);
        teacherID = intent.getStringExtra(TEACHERID);

        tv_accessCode = findViewById(R.id.tv_accessCode);
        clipBoard = findViewById(R.id.img_copy);

        //copy to clipboard
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

        //delete class
        classDelete = findViewById(R.id.classDelete);
        classDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClass();
            }
        });

        //Toolbar
        Toolbar toolbar = findViewById(R.id.student_classroomToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(className);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Query for Recyclerview
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

        //onclick
        adapter.setOnItemClickListener(new TeacherQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                QuizModel quizModel = documentSnapshot.toObject(QuizModel.class);
                String quizid = documentSnapshot.getString("quizId");
                String quizName = documentSnapshot.getString("quizName");
                Intent intent = new Intent (TeacherClassRoomActivity.this,TeacherCreateQuestionActivity.class);
                intent.putExtra(TeacherCreateQuestionActivity.QUIZNAME,quizName);
                intent.putExtra(TeacherCreateQuestionActivity.QUIZID,quizid);
                intent.putExtra(TeacherCreateQuestionActivity.CLASSID, classID);
                startActivity(intent);
            }
        });

        //floating buttons
        FloatingActionButton createQuizBtn = findViewById(R.id.createQuizBtn);
        FloatingActionButton btnStudentList = findViewById(R.id.student_List);
        FloatingActionButton btnScore = findViewById(R.id.Score);

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherClassRoomActivity.this, StudentScoreActivity.class);
                intent.putExtra(StudentScoreActivity.CLASSNAME, className);
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
                intent.putExtra(StudentListActivity.CLASSNAME, className);
                startActivity(intent);
            }
        });

        createQuizBtn.setOnClickListener(new View.OnClickListener() {
            EditText etCreateQuiz;
            Button btnCreateQuiz;
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(TeacherClassRoomActivity.this);
                View createQuizView = getLayoutInflater().inflate(R.layout.pop_up_window_create_quiz, null);

                etCreateQuiz= createQuizView.findViewById(R.id.et_create_quiz);
                btnCreateQuiz= createQuizView.findViewById(R.id.btn_create_quiz);

                Button btnCancelQuiz = createQuizView.findViewById(R.id.btn_cancel_quiz);
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

    private void deleteClass() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeacherClassRoomActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        dialogBuilder.setView(view);
        AlertDialog dialog = dialogBuilder.create();
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView delete = view.findViewById(R.id.delete);
        TextView cancel = view.findViewById(R.id.cancel);
        title.setText("Are you sure?");
        message.setText("You are about to delete this class.");
        dialog.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference classRef = app_fireStore.collection("CLASSES")
                        .document(classID);
                classRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Class delete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Deleting class failed");
                    }
                });

                CollectionReference quizRef = app_fireStore.collection("QUIZLIST");
                Query quizQuery = quizRef.whereEqualTo("classId", classID);
                quizQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = app_fireStore.batch();
                        List<DocumentSnapshot> snapshotList= queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: snapshotList){
                            batch.delete(documentSnapshot.getReference());
                        }
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Quizzes delete");
                                }else{
                                    Log.d(TAG, "Error");
                                }
                            }
                        });
                    }
                });

                CollectionReference studQuizRef = app_fireStore.collection("STUDENT_QUIZ");
                Query studQuizQuery = studQuizRef.whereEqualTo("classID", classID);
                studQuizQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = app_fireStore.batch();
                        List<DocumentSnapshot> snapshotList= queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: snapshotList){
                            batch.delete(documentSnapshot.getReference());
                        }
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Class quizzes delete");
                                }else{
                                    Log.d(TAG, "Error");
                                }
                            }
                        });
                    }
                });

                CollectionReference studRef = app_fireStore.collection("STUDENTS");
                Query studQuery = studRef.whereEqualTo("classID", classID);
                studQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = app_fireStore.batch();
                        List<DocumentSnapshot> snapshotList= queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: snapshotList){
                            batch.delete(documentSnapshot.getReference());
                        }
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Class delete");
                                }else{
                                    Log.d(TAG, "Error");
                                }
                            }
                        });
                    }
                });
                onBackPressed();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void addNewQuiz(String quizName, String classID){
        DbQuery.createQuizName(quizName, classID, teacherID, className, new MyCompleteListener() {
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
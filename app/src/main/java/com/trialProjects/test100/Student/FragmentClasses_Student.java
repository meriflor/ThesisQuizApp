package com.trialProjects.test100.Student;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.trialProjects.test100.FirebaseServices.DbQuery;
import com.trialProjects.test100.Listener.MyCompleteListener;
import com.trialProjects.test100.R;

public class FragmentClasses_Student extends Fragment {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private RecyclerView recyclerView;
    private EditText classCode;
    private Button btn_join, btn_cancel;
    private View view;
    private JoinClassAdapter adapter;

    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_classes__student, container, false);
        fab = view.findViewById(R.id.student_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();
            }
        });

        getClassListView();
        return view;
    }

    private void getClassListView() {
        CollectionReference classRef = FirebaseFirestore
                .getInstance()
                .collection("STUDENTS");
        Query classQuery = classRef
                .whereEqualTo("studentID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("className", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<JoinClasses> options = new FirestoreRecyclerOptions.Builder<JoinClasses>()
                .setQuery(classQuery, JoinClasses.class)
                .build();
        adapter = new JoinClassAdapter(options);

        recyclerView = view.findViewById(R.id.student_classListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new JoinClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                JoinClasses classes = documentSnapshot.toObject(JoinClasses.class);
                String classroomID = documentSnapshot.getString("classID");
                String TeacherID =documentSnapshot.getString("teacherID");
                String className = documentSnapshot.getString("className");
                String studentName = documentSnapshot.getString("studentName");
                String studentID = documentSnapshot.getString("studentID");
                Intent intent = new Intent(getContext(), StudentClassRoomActivity.class);
                intent.putExtra(StudentClassRoomActivity.CLASSNAME,className);
                intent.putExtra(StudentClassRoomActivity.CLASSROOMID,classroomID);
                intent.putExtra(StudentClassRoomActivity.STUDENTNAME, studentName);
                intent.putExtra(StudentClassRoomActivity.STUDENTID, studentID);

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

    private void showJoinDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        View joinClassView = getLayoutInflater().inflate(R.layout.pop_up_window_join, null);
        
        classCode = joinClassView.findViewById(R.id.student_joinCode);
        btn_join = joinClassView.findViewById(R.id.student_btnJoin);
        btn_cancel = joinClassView.findViewById(R.id.student_btnCancel);
        
        dialogBuilder.setView(joinClassView);
        dialog = dialogBuilder.create();
        dialog.show();
        
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classID = classCode.getText().toString().trim();
                if(classID.isEmpty()){
                    Toast.makeText(getContext(),
                            "Please enter the access code of your class",
                            Toast.LENGTH_SHORT)
                            .show();
                }else{
                    checkClassExists(classID);
                    dialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void checkClassExists(String classID) {
        DbQuery.checkClassExist(classID, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Class Exists");
                checkStudentClassExist(classID);
            }
            @Override
            public void onFailure() {
                Log.d(TAG, "Class does not exists");
            }
        });
    }

    private void checkStudentClassExist(String classID) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        String studentID = firebaseAuth.getCurrentUser().getUid();
        DbQuery.checkStudentClassExist(classID, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "This class already exist in your list");
            }
            @Override
            public void onFailure() {
                joinClassSuccessfully(classID, studentID);
            }
        });
    }

    private void joinClassSuccessfully(String classID, String studentID) {

        DocumentReference studRef = FirebaseFirestore.getInstance()
                .collection("USERS").document(studentID);
        studRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot studDoc = task.getResult();
                    String studentName = studDoc.getString("FULL_NAME");

                    DbQuery.joinClass(classID, studentID, studentName, new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Class joined",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            Log.d(TAG, "Error occurred");
                        }
                    });

                    DbQuery.joinUpdateQuiz(classID, studentID, new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Quiz loaded successfully");
                        }

                        @Override
                        public void onFailure() {
                            Log.d(TAG, "Error Occurred");
                        }
                    });
                }
            }
        });

    }
}
package com.trialProjects.test100;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.activities.JoinClasses;

public class FragmentClasses_Student extends Fragment {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    EditText classCode;
    Button btn_join, btn_cancel;
    View view;
    JoinClassAdapter adapter;

    FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_classes__student, container, false);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClass();
            }
        });

        setUpRecyclerView();
        getClassList();

        return view;
    }

    private void getClassList() {

        CollectionReference classRef = FirebaseFirestore.getInstance().collection("Students");
        Query classQuery = classRef
                .whereEqualTo("studentID", FirebaseAuth.getInstance().getCurrentUser().getUid());



        FirestoreRecyclerOptions<JoinClasses> options = new FirestoreRecyclerOptions.Builder<JoinClasses>().setQuery(classQuery, JoinClasses.class).build();
        adapter = new JoinClassAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void joinClass(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        View joinClassView = getLayoutInflater().inflate(R.layout.pop_up_window_join, null);

        classCode = joinClassView.findViewById(R.id.et_class_code);
        btn_join = joinClassView.findViewById(R.id.btn_join);
        btn_cancel = joinClassView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(joinClassView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classID = classCode.getText().toString().trim();

                if(classID.isEmpty())
                {
                    Toast.makeText(getContext(),"Please enter the access code of your class",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    joinNewClass(classID);
                    dialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


///////////
    private void joinNewClass(String classID) {

        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference classRef = FirebaseFirestore.getInstance().collection("CLASSES");
        Query classQuery = classRef
                .whereEqualTo("classID", classID);
        classQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    DocumentReference addClassRef = FirebaseFirestore.getInstance().collection("Students")
                            .document();

                    JoinClasses classes = new JoinClasses();
                    classes.setStudentID(studentID);
                    classes.setClassID(classID);

                    addClassRef.set(classes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "Class Joined");
                                Toast.makeText(getContext(), "Joined Class", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Class not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });



        /*CollectionReference classRef = FirebaseFirestore.getInstance().collection("CLASSES");
        classRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.equals(classID)){
                            Log.d(TAG, "lahossdfsd?");
                            DocumentReference addClassRef = FirebaseFirestore.getInstance().collection("Students")
                                    .document();

                            JoinClasses classes = new JoinClasses();
                            classes.setStudentID(studentID);
                            classes.setclassID(classID);

                            addClassRef.set(classes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d(TAG, "Class Joined");
                                        Toast.makeText(getContext(), "Joined Class", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "Class not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}
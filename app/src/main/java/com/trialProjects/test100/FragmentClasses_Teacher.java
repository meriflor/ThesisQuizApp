package com.trialProjects.test100;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentClasses_Teacher extends Fragment {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText className, classSection;
    private Button btn_create, btn_cancel;

    private FloatingActionButton fab;

    private FirebaseAuth app_auth;
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private CollectionReference classesRef = app_fireStore.collection("CLASSES");
    private ClassesAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_classes_teacher, container, false);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView(){
        Query classList = classesRef.orderBy("className", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Classes> options = new FirestoreRecyclerOptions.Builder<Classes>().setQuery(classList, Classes.class).build();

        adapter = new ClassesAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private void createClass(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        View createClassView = getLayoutInflater().inflate(R.layout.pop_up_window_create, null);

        className = createClassView.findViewById(R.id.et_class_name);
        classSection = createClassView.findViewById(R.id.et_class_section);
        btn_create = createClassView.findViewById(R.id.btn_create);
        btn_cancel = createClassView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(createClassView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
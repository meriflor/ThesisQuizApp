package com.trialProjects.test100;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentClasses_Teacher extends Fragment {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText className, classSection;
    Button btn_create, btn_cancel;

    FloatingActionButton fab;

    FirebaseAuth app_auth;
    FirebaseFirestore app_fireStore;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classes_teacher, container, false);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        return view;
    }

    public void createClass(){
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
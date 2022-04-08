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

public class FragmentClasses_Student extends Fragment {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    EditText classCode;
    Button btn_join, btn_cancel;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classes__student, container, false);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClass();
            }
        });


        return view;
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
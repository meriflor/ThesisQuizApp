package com.trialProjects.test100;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AddClassesAdapter extends FirestoreRecyclerAdapter<AddClasses, AddClassesAdapter.ClassesHolder> {

    public AddClassesAdapter(@NonNull FirestoreRecyclerOptions<AddClasses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassesHolder classesHolder, int i, @NonNull AddClasses classes) {
        classesHolder.className.setText(classes.getClassName());
        classesHolder.classSection.setText(classes.getClassSection());
        classesHolder.accessCode.setText(classes.getAccessCode());
    }

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassesHolder(view);
    }

    class ClassesHolder extends RecyclerView.ViewHolder{
        TextView className, classSection, accessCode;

        public ClassesHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.tv_class_name);
            classSection = itemView.findViewById(R.id.tv_class_section);
            accessCode = itemView.findViewById(R.id.tv_access_code);
        }
    }

}

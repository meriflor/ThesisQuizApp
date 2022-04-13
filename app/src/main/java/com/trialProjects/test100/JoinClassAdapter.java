package com.trialProjects.test100;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.trialProjects.test100.activities.JoinClasses;

public class JoinClassAdapter extends FirestoreRecyclerAdapter<JoinClasses, JoinClassAdapter.ClassesHolder> {

    public JoinClassAdapter(@NonNull FirestoreRecyclerOptions<JoinClasses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassesHolder classesHolder, int i, @NonNull JoinClasses joinClasses) {
        classesHolder.accessCode.setText(joinClasses.getClassID());
    }

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassesHolder(view);
    }

    public class ClassesHolder extends RecyclerView.ViewHolder {
        TextView className, classSection, accessCode;

        public ClassesHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.tv_class_name);
            classSection = itemView.findViewById(R.id.tv_class_section);
            accessCode = itemView.findViewById(R.id.tv_access_code);
        }
    }
}

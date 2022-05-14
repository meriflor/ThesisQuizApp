package com.trialProjects.test100.Teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.trialProjects.test100.R;

public class StudentListAdapter extends FirestoreRecyclerAdapter<StudentListModel, StudentListAdapter.ListHolder>{

    public StudentListAdapter(@NonNull FirestoreRecyclerOptions<StudentListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListHolder listHolder, int i, @NonNull StudentListModel studentListModel) {
        listHolder.studentName.setText(studentListModel.getStudentName());
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new ListHolder(view);
    }

    public static class ListHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName_list);
        }
    }
}

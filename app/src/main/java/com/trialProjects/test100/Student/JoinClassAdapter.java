package com.trialProjects.test100.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.trialProjects.test100.R;

public class JoinClassAdapter extends FirestoreRecyclerAdapter<JoinClasses, JoinClassAdapter.ClassesHolder> {

    public JoinClassAdapter(@NonNull FirestoreRecyclerOptions<JoinClasses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassesHolder classesHolder, int i, @NonNull JoinClasses joinClasses) {
        classesHolder.className.setText(joinClasses.getClassName());
        classesHolder.classSection.setText(joinClasses.getClassSection());
    }

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_item,
                parent, false);
        return new ClassesHolder(view, mListener);
    }
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
            void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){mListener = listener;};
    class ClassesHolder extends RecyclerView.ViewHolder {
        TextView className, classSection;

        public ClassesHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            className = itemView.findViewById(R.id.studentClassName);
            classSection = itemView.findViewById(R.id.studentClassSection);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                listener.onItemClick(getSnapshots().getSnapshot(position),position);
                            }
                    }
                }
            });
        }
    }
}

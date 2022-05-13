package com.trialProjects.test100.Teacher;

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

public class TeacherQuizAdapter extends FirestoreRecyclerAdapter<QuizModel, TeacherQuizAdapter.QuizHolder>{

    public TeacherQuizAdapter(@NonNull FirestoreRecyclerOptions<QuizModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuizHolder quizHolder, int i, @NonNull QuizModel quizModel) {
        quizHolder.quizName.setText(quizModel.getQuizName());
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new QuizHolder(view, mListener);
    }
        private OnItemClickListener mListener;
        public interface OnItemClickListener{
                void onItemClick(DocumentSnapshot documentSnapshot, int position);
        }
        public void setOnItemClickListener (OnItemClickListener listener) { mListener = listener;}

    public class QuizHolder extends RecyclerView.ViewHolder {
        TextView quizName;
        public QuizHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            quizName = itemView.findViewById(R.id.tv_quizName);

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


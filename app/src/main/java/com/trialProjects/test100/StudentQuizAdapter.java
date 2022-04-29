package com.trialProjects.test100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudentQuizAdapter extends FirestoreRecyclerAdapter<StudentQuizModel, StudentQuizAdapter.QuizHolder> {


    public StudentQuizAdapter(@NonNull FirestoreRecyclerOptions<StudentQuizModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuizHolder quizHolder, int i, @NonNull StudentQuizModel studentQuizModel) {
        quizHolder.quizName.setText(studentQuizModel.getQuizName());
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new QuizHolder(view);
    }

    public class QuizHolder extends RecyclerView.ViewHolder {
       TextView quizName;
        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.tv_quizName);

        }
    }
}

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

public class ScoreAdapter extends FirestoreRecyclerAdapter<ScoreModel, ScoreAdapter.ScoreHolder> {

    public ScoreAdapter(@NonNull FirestoreRecyclerOptions<ScoreModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ScoreHolder scoreHolder, int i, @NonNull ScoreModel scoreModel) {
        scoreHolder.studentName.setText(scoreModel.getStudentName());
        scoreHolder.studentScore.setText(scoreModel.getScore());
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_score_item, parent, false);
        return new ScoreHolder(view);
    }

    public class ScoreHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView studentScore;
        public ScoreHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.score_studentName);
            studentScore = itemView.findViewById(R.id.score_studentScore);

        }
    }
}

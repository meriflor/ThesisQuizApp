package com.trialProjects.test100.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.trialProjects.test100.R;

public class StudentQuestionsAdapter extends FirestoreRecyclerAdapter<StudentQuestionsModel, StudentQuestionsAdapter.QuestionsHolder> {

    public StudentQuestionsAdapter(@NonNull FirestoreRecyclerOptions<StudentQuestionsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionsHolder questionsHolder, int i, @NonNull StudentQuestionsModel studentQuestionsModel) {
        questionsHolder.questionName.setText(studentQuestionsModel.getQuestion());
    }

    @NonNull
    @Override
    public QuestionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,
                parent, false);
        return new QuestionsHolder(view);
    }

    public class QuestionsHolder extends RecyclerView.ViewHolder {
        TextView questionName;
        public QuestionsHolder(@NonNull View itemView) {
            super(itemView);
            questionName = itemView.findViewById(R.id.tv_questionName);
        }
    }
}

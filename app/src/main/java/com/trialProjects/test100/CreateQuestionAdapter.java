package com.trialProjects.test100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CreateQuestionAdapter  extends FirestoreRecyclerAdapter<QuestionModel,CreateQuestionAdapter.QuestionHolder> {

    public CreateQuestionAdapter(@NonNull FirestoreRecyclerOptions<QuestionModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionHolder questionHolder, int i, @NonNull QuestionModel questionModel) {
        questionHolder.questionName.setText(questionModel.getQuestion());
        questionHolder.optionA.setText("a. " + questionModel.getOptionA());
        questionHolder.optionB.setText("b. " + questionModel.getOptionB());
        questionHolder.optionC.setText("c. " + questionModel.getOptionC());
        questionHolder.optionD.setText("d. " + questionModel.getOptionD());
        questionHolder.answer.setText("Answer: " + questionModel.getAnswer());
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionHolder(view);

    }

    public class QuestionHolder extends RecyclerView.ViewHolder {
        TextView questionName, optionA, optionB, optionC, optionD, answer;
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            questionName = itemView.findViewById(R.id.tv_questionName);
            optionA = itemView.findViewById(R.id.tv_optionA);
            optionB = itemView.findViewById(R.id.tv_optionB);
            optionC = itemView.findViewById(R.id.tv_optionC);
            optionD = itemView.findViewById(R.id.tv_optionD);
            answer = itemView.findViewById(R.id.tv_answer);
        }
    }
}

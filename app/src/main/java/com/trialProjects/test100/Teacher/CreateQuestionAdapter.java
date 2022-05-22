package com.trialProjects.test100.Teacher;

import android.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trialProjects.test100.R;

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

        questionHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        questionHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_delete, null);
                dialogBuilder.setView(view);
                AlertDialog dialog = dialogBuilder.create();
                TextView title = view.findViewById(R.id.title);
                TextView message = view.findViewById(R.id.message);
                TextView delete = view.findViewById(R.id.delete);
                TextView cancel = view.findViewById(R.id.cancel);

                title.setText("Are you sure?");
                message.setText("You are about to delete this question.");
                dialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference quesRef = FirebaseFirestore.getInstance()
                                .collection("QUESTIONS")
                                .document(getSnapshots().getSnapshot(questionHolder.getAdapterPosition()).getId());
                        quesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "Question deleted");
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", e.getMessage());
                            }
                        });
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionHolder(view);

    }

    public class QuestionHolder extends RecyclerView.ViewHolder {
        TextView questionName, optionA, optionB, optionC, optionD, answer, edit, delete;
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            questionName = itemView.findViewById(R.id.tv_questionName);
            optionA = itemView.findViewById(R.id.tv_optionA);
            optionB = itemView.findViewById(R.id.tv_optionB);
            optionC = itemView.findViewById(R.id.tv_optionC);
            optionD = itemView.findViewById(R.id.tv_optionD);
            answer = itemView.findViewById(R.id.tv_answer);

            edit = itemView.findViewById(R.id.tv_edit_question);
            delete = itemView.findViewById(R.id.tv_delete_question);
        }
    }
}

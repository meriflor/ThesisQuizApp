package com.trialProjects.test100.Student;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.R;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String QUIZNAME = "Quiz Name";
    public static final String STUDENTNAME = "Student name";
    public static final String STUDENTID = "studID";
    public static String QUIZID = "QuizID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private TextView questionCount, question;
    private Button optionA, optionB, optionC, optionD;
    private List<StudentQuestionsModel> questionList;
    private String quizID, quizName, studentName, studentID;
    int quesNum;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);

        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        quizName = intent.getStringExtra(QUIZNAME);
        studentName = intent.getStringExtra(STUDENTNAME);
        studentID = intent.getStringExtra(STUDENTID);

        questionCount = findViewById(R.id.question_count);
        question = findViewById(R.id.question_name);
        optionA = findViewById(R.id.btn_optionA);
        optionB = findViewById(R.id.btn_optionB);
        optionC = findViewById(R.id.btn_optionC);
        optionD = findViewById(R.id.btn_optionD);

        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);

        getQuestion();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentQuestionsActivity.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to exit app? Your progress won't be saved. You only have 1 attempt.");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getQuestion() {
        questionList = new ArrayList<>();
        Log.d(TAG, quizID);

        CollectionReference quesRef = firestore.collection("QUESTIONS");
        Query quesQuery = quesRef.whereEqualTo("quizId", quizID);
        quesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot questions = task.getResult();
                    for(QueryDocumentSnapshot doc: questions){
                        questionList.add(new StudentQuestionsModel(
                                doc.getString("question"),
                                doc.getString("optionA"),
                                doc.getString("optionB"),
                                doc.getString("optionC"),
                                doc.getString("optionD"),
                                Integer.valueOf(doc.getString("answer"))
                        ));
                    }
                    setQuestion();
                }else{
                    Toast.makeText(StudentQuestionsActivity.this,
                            task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d(TAG, String.valueOf(questionList.size()));
    }

    private void setQuestion() {

        question.setText(questionList.get(0).getQuestion());
        optionA.setText(questionList.get(0).getOptionA());
        optionB.setText(questionList.get(0).getOptionB());
        optionC.setText(questionList.get(0).getOptionC());
        optionD.setText(questionList.get(0).getOptionD());

        questionCount.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));
        quesNum = 0;
        score = 0;
    }


    @Override
    public void onClick(View v) {
        int selectedOption = 0;
        switch (v.getId()){
            case R.id.btn_optionA:
                selectedOption = 1;
                break;
            case R.id.btn_optionB:
                selectedOption = 2;
                break;
            case R.id.btn_optionC:
                selectedOption = 3;
                break;
            case R.id.btn_optionD:
                selectedOption = 4;
                break;
            default:

        }
        checkAnswer(selectedOption);
    }

    private void checkAnswer(int selectedOption) {
        if (selectedOption == questionList.get(quesNum).getAnswer()){
            score++;
            Log.d(TAG, String.valueOf(score));
        }
        changeQuestion();
    }

    private void changeQuestion() {
        if(quesNum < questionList.size() - 1){
            quesNum ++;
            playAnim(question, 0, 0);
            playAnim(optionA, 0,1);
            playAnim(optionB, 0, 2);
            playAnim(optionC, 0, 3);
            playAnim(optionD, 0, 4);

            questionCount.setText(String.valueOf(quesNum+1) + "/" + String.valueOf(questionList.size()));
        }else{
            Intent intent = new Intent(StudentQuestionsActivity.this,
                    StudentResultActivity.class);
            intent.putExtra(StudentResultActivity.SCORE, String.valueOf(score));
            intent.putExtra(StudentResultActivity.SCORE_OVER, String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(StudentResultActivity.STUDENTNAME, studentName);
            intent.putExtra(StudentResultActivity.QUIZID, quizID);
            intent.putExtra(StudentResultActivity.STUDENTID, studentID);
            startActivity(intent);
        }
    }

    private void playAnim(View view, final int value, int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value)
                .setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0){
                            switch (viewNum){
                                case 0:
                                    ((TextView)view).setText(questionList.
                                            get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.
                                            get(quesNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.
                                            get(quesNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.
                                            get(quesNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.
                                            get(quesNum).getOptionD());
                                    break;
                            }
                            playAnim(view, 1, viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}
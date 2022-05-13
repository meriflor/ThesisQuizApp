package com.trialProjects.test100;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String QUIZNAME = "Quiz Name";
    public static String QUIZID = "QuizID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private TextView questionCount, question;
    private Button optionA, optionB, optionC, optionD;
    private List<StudentQuestionsModel> questionList;
    private String quizID, quizName;
    int quesNum;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);

        Intent intent = getIntent();
        quizID = intent.getStringExtra(QUIZID);
        quizName = intent.getStringExtra(QUIZNAME);

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

    private void getQuestion() {
        questionList = new ArrayList<>();
        Log.d(TAG, quizID);

//        questionList.add(new StudentQuestionsModel("Rarararara", "a", "b", "c", "d", 1));
//        questionList.add(new StudentQuestionsModel("adgagadgasdg", "asfsf", "ggggb", "cas", "d", 1));
//        questionList.add(new StudentQuestionsModel("Raraerwerwere  feferarara", "a", "asfsab", "ffc", "d", 1));
//        Log.d(TAG, String.valueOf(questionList.size()));
//        setQuestion();


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
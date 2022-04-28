package com.trialProjects.test100;

public class QuizModel {
    private String quizName;


    public QuizModel(){

    }

    public QuizModel(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
}

package com.trialProjects.test100.Student;

public class StudentQuizModel {
    private String quizName, quizId;

    public StudentQuizModel() {
    }

    public StudentQuizModel(String quizName, String quizId) {
        this.quizName = quizName;
        this.quizId = quizId;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getQuizName() {
        return quizName;
    }
}

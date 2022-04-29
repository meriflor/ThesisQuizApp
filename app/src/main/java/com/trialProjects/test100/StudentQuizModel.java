package com.trialProjects.test100;

public class StudentQuizModel {
    private String quizName;

    public StudentQuizModel(String quizName, String classId, String quizId){

    }

    public StudentQuizModel(String name, String classId, String studentId, String quizName) {
        this.quizName = quizName;
    }

    public String getQuizName() {
        return quizName;
    }
}

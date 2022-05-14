package com.trialProjects.test100.Teacher;

public class ScoreModel {
    private String score, studentName;

    public ScoreModel() {
    }

    public ScoreModel(String score, String studentName) {
        this.score = score;
        this.studentName = studentName;
    }

    public String getScore() {
        return score;
    }

    public String getStudentName() {
        return studentName;
    }
}

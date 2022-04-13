package com.trialProjects.test100.activities;

public class JoinClasses {

    private String classID, studentID, className, classSection;

    public JoinClasses(){

    }

    public JoinClasses(String classID, String studentID, String className, String classSection) {
        this.classID = classID;
        this.studentID = studentID;
        this.className = className;
        this.classSection = classSection;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassSection() {
        return classSection;
    }
}

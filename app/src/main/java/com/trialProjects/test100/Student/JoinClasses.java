package com.trialProjects.test100.Student;

public class JoinClasses {

    private String classID, studentID, studentName, className, classSection;

    public JoinClasses(){

    }

    public JoinClasses(String classID, String studentID, String studentName, String className, String classSection) {
        this.classID = classID;
        this.studentID = studentID;
        this.studentName = studentName;
        this.className = className;
        this.classSection = classSection;
    }

    public String getClassID() {
        return classID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassSection() {
        return classSection;
    }

    public String getStudentName() {
        return studentName;
    }
}

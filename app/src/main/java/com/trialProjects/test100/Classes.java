package com.trialProjects.test100;

public class Classes {

    private String className, classSection, accessCode, teacherID, classID;

    public Classes(){

    }

    public Classes(String className, String classSection, String accessCode, String teacherID, String classID){
        this.className = className;
        this.classSection = classSection;
        this.accessCode = accessCode;
        this.teacherID = teacherID;
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassSection() {
        return classSection;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}

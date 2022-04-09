package com.trialProjects.test100;

public class Classes {

    private String className, classSection, accessCode;

    public Classes(){

    }

    public Classes(String className, String classSection, String accessCode){
        this.className = className;
        this.classSection = classSection;
        this.accessCode = accessCode;
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
}

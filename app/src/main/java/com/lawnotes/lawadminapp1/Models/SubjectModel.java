package com.lawnotes.lawadminapp1.Models;

public class SubjectModel {

    private String subjectName;
    private String key;

    public SubjectModel(String subjectName) {
        this.subjectName = subjectName;
    }

    public SubjectModel() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

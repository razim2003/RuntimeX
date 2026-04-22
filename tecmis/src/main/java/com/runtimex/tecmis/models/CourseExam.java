package com.runtimex.tecmis.models;

public class CourseExam {
    private String courseCode;
    private String examTypeId;
    private double weight;
    private String examDate;
    private String examName;
    private String examTypeName;

    public CourseExam() {}

    public CourseExam(String courseCode, String examTypeId, double weight, String examDate, String examName) {
        this.courseCode = courseCode;
        this.examTypeId = examTypeId;
        this.weight = weight;
        this.examDate = examDate;
        this.examName = examName;
    }

    public String getCourseCode() { return courseCode; }
    public String getExamTypeId() { return examTypeId; }
    public double getWeight() { return weight; }
    public String getExamDate() { return examDate; }
    public String getExamName() { return examName; }
    public String getExamTypeName() { return examTypeName; }

    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setExamTypeId(String examTypeId) { this.examTypeId = examTypeId; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setExamDate(String examDate) { this.examDate = examDate; }
    public void setExamName(String examName) { this.examName = examName; }
    public void setExamTypeName(String examTypeName) { this.examTypeName = examTypeName; }
}
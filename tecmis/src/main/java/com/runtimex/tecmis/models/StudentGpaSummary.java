package com.runtimex.tecmis.models;

public class StudentGpaSummary {
    private String studentId;
    private String studentName;
    private double sgpa;
    private double cgpa;

    public StudentGpaSummary() {
    }

    public StudentGpaSummary(String studentId, String studentName, double sgpa, double cgpa) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.sgpa = sgpa;
        this.cgpa = cgpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getSgpa() {
        return sgpa;
    }

    public void setSgpa(double sgpa) {
        this.sgpa = sgpa;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }
}

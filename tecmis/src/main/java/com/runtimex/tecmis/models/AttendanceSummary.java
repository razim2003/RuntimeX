package com.runtimex.tecmis.models;

public class AttendanceSummary {
    private String studentId;
    private String studentName;
    private int totalSessions;
    private int presentSessions;
    private int approvedMedicalAbsences;
    private int effectivePresent;
    private double percentage;
    private boolean eligible;

    public AttendanceSummary() {
    }

    public AttendanceSummary(String studentId, String studentName, int totalSessions, int presentSessions,
            int approvedMedicalAbsences) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalSessions = totalSessions;
        this.presentSessions = presentSessions;
        this.approvedMedicalAbsences = approvedMedicalAbsences;
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

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getPresentSessions() {
        return presentSessions;
    }

    public void setPresentSessions(int presentSessions) {
        this.presentSessions = presentSessions;
    }

    public int getApprovedMedicalAbsences() {
        return approvedMedicalAbsences;
    }

    public void setApprovedMedicalAbsences(int approvedMedicalAbsences) {
        this.approvedMedicalAbsences = approvedMedicalAbsences;
    }

    public int getEffectivePresent() {
        return effectivePresent;
    }

    public void setEffectivePresent(int effectivePresent) {
        this.effectivePresent = effectivePresent;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
}

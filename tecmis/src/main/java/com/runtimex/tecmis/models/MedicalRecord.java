package com.runtimex.tecmis.models;

public class MedicalRecord {
    private String refNo;
    private String studentId;
    private String reason;
    private String status;
    private String startDate;
    private String endDate;
    private String proofImagePath;

    public MedicalRecord() {
    }

    public MedicalRecord(String refNo, String studentId, String reason, String status, String startDate,
            String endDate) {
        this.refNo = refNo;
        this.studentId = studentId;
        this.reason = reason;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public MedicalRecord(String refNo, String studentId, String reason, String status, String startDate,
            String endDate, String proofImagePath) {
        this.refNo = refNo;
        this.studentId = studentId;
        this.reason = reason;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.proofImagePath = proofImagePath;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProofImagePath() {
        return proofImagePath;
    }

    public void setProofImagePath(String proofImagePath) {
        this.proofImagePath = proofImagePath;
    }
}

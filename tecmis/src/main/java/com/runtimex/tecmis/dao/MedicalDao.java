package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.MedicalRecord;

import java.util.List;

public interface MedicalDao {
    void addMedical(MedicalRecord record);

    void updateMedical(MedicalRecord record);

    void updateMedicalStatus(String refNo, String status);

    void linkMedicalToAttendance(String attendanceId, String refNo);

    List<MedicalRecord> findMedicalByStudent(String studentId);
}

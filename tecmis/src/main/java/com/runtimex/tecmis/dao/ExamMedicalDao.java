package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.ExamMedical;
import java.util.List;

public interface ExamMedicalDao {
    void create(ExamMedical medical);

    List<ExamMedical> getPending();

    List<ExamMedical> getByStudent(String stuId);

    void updateStatus(String refNo, String status);

    boolean exists(String stuId, String courseCode, String typeId);

    ExamMedical getByRef(String refNo);

    boolean enrollmentExists(String stuId, String courseCode);

    boolean courseExamExists(String courseCode, String typeId);

    boolean hasMark(String stuId, String courseCode, String typeId);
}

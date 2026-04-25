package com.runtimex.tecmis.services.interfaces;

import com.runtimex.tecmis.models.ExamMedical;
import java.util.List;

public interface ExamMedicalService {
    void submitMedical(String stuId, String courseCode, String typeId, String proofImagePath);

    List<ExamMedical> getPending();

    List<ExamMedical> getByStudent(String stuId);

    void updateStatus(String refNo, String status);

    boolean hasExistingRequest(String stuId, String courseCode, String typeId);

    boolean validateEligibility(String stuId, String courseCode, String typeId);
}

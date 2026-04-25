package com.runtimex.tecmis.services.impl;

import com.runtimex.tecmis.dao.ExamMedicalDao;
import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.ExamMedical;
import com.runtimex.tecmis.services.interfaces.ExamMedicalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ExamMedicalServiceImpl implements ExamMedicalService {

    private static final Set<String> QUIZ_TYPES = Set.of("QU01", "QU02", "QU03");
    private static final Set<String> ALLOWED_TYPES = Set.of("MID", "FIN", "ASST");

    private final ExamMedicalDao examMedicalDao;
    private final MarksDao marksDao;

    public ExamMedicalServiceImpl(ExamMedicalDao examMedicalDao, MarksDao marksDao) {
        this.examMedicalDao = examMedicalDao;
        this.marksDao = marksDao;
    }

    @Override
    public void submitMedical(String stuId, String courseCode, String typeId, String proofImagePath) {
        validateEligibility(stuId, courseCode, typeId);
        if (proofImagePath == null || proofImagePath.isBlank()) {
            throw new IllegalArgumentException("Medical proof image is required");
        }

        String refNo = generateRefNo();
        CourseExam exam = new CourseExam();
        exam.setCourseCode(courseCode);
        exam.setExamTypeId(typeId);

        ExamMedical medical = new ExamMedical(
            refNo,
            stuId,
            exam,
            "Pending",
            LocalDate.now().toString(),
            proofImagePath);
        examMedicalDao.create(medical);
    }

    @Override
    public List<ExamMedical> getPending() {
        return examMedicalDao.getPending();
    }

    @Override
    public List<ExamMedical> getByStudent(String stuId) {
        if (stuId == null || stuId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required");
        }
        return examMedicalDao.getByStudent(stuId);
    }

    @Override
    public void updateStatus(String refNo, String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        ExamMedical medical = examMedicalDao.getByRef(refNo);
        if (medical == null) {
            throw new RuntimeException("Exam medical not found for ref: " + refNo);
        }

        if ("Approved".equalsIgnoreCase(status)) {
            String stuId = medical.getStudentId();
            String courseCode = medical.getCourseExam().getCourseCode();
            String typeId = medical.getCourseExam().getExamTypeId();
            if (examMedicalDao.hasMark(stuId, courseCode, typeId)) {
                throw new RuntimeException("Cannot approve medical when marks already exist.");
            }
        }

        examMedicalDao.updateStatus(refNo, status);
    }

    @Override
    public boolean hasExistingRequest(String stuId, String courseCode, String typeId) {
        return examMedicalDao.exists(stuId, courseCode, typeId);
    }

    @Override
    public boolean validateEligibility(String stuId, String courseCode, String typeId) {
        if (stuId == null || stuId.isBlank() || courseCode == null || courseCode.isBlank()
                || typeId == null || typeId.isBlank()) {
            throw new IllegalArgumentException("Student, course, and exam type are required");
        }

        if (QUIZ_TYPES.contains(typeId)) {
            throw new RuntimeException("Medical requests are not allowed for quiz exams");
        }
        if (!ALLOWED_TYPES.contains(typeId)) {
            throw new RuntimeException("Medical requests are allowed only for MID, FIN, and ASST exams");
        }
        if (!examMedicalDao.enrollmentExists(stuId, courseCode)) {
            throw new RuntimeException("Student is not enrolled in the course");
        }
        if (!examMedicalDao.courseExamExists(courseCode, typeId)) {
            throw new RuntimeException("Course exam does not exist for the given type");
        }
        if (examMedicalDao.exists(stuId, courseCode, typeId)) {
            throw new RuntimeException("Exam medical already submitted for this exam");
        }
        if (examMedicalDao.hasMark(stuId, courseCode, typeId)) {
            throw new RuntimeException("Cannot submit medical when marks already exist");
        }
        if (marksDao.hasApprovedExamMedical(stuId, courseCode, typeId)) {
            throw new RuntimeException("Approved medical already exists for this exam");
        }
        return true;
    }

    private String generateRefNo() {
        return "EM" + String.format("%09d", Math.abs(System.nanoTime() % 1_000_000_000L));
    }
}

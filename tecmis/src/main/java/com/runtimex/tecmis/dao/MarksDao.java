package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.models.MarkEntry;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.StudentInfo;
import java.util.List;

public interface MarksDao {

    // Get all marks for a student in a specific course
    List<Mark> getMarksByStudentAndCourse(String stuId, String courseCode);

    // Get a single mark (useful for updates / validation)
    Mark getMark(String stuId, String courseCode, String examTypeId);

    // Insert new mark record
    void addMark(Mark mark);

    // Update existing mark value
    void updateMark(Mark mark);

    // Delete mark by ID
    void deleteMark(String markId);

    // Load all course exams for a course
    List<CourseExam> getCourseExams(String courseCode);

    // Load all courses in the system
    List<CourseUnit> getAllCourses();

    // Load courses for a student
    List<CourseUnit> getCoursesByStudent(String stuId);

    // Load students enrolled to a course
    List<StudentInfo> getStudentsByCourse(String courseCode);

    // Load marks with nullable values
    List<MarkEntry> getMarkEntries(String stuId, String courseCode);

    // Load a single mark entry with nullable value
    MarkEntry getMarkEntry(String stuId, String courseCode, String examTypeId);

    // Undergraduate status (Proper/Repeat/Suspended)
    String getUndergraduateStatus(String stuId);

    // Exam medical status
    boolean hasApprovedExamMedical(String stuId, String courseCode, String examTypeId);

    // Any exam medical submitted for a course
    boolean hasExamMedicalForCourse(String stuId, String courseCode);

}
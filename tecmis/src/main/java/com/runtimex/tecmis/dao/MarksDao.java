package com.runtimex.tecmis.dao;

import java.util.List;
import com.runtimex.tecmis.models.Mark;

public interface MarksDao {

    // Fetch all marks for a student in a course
    List<Mark> getMarksByStudentAndCourse(String stuId, String courseCode);

   List<Mark> getQuizMarksByStudentAndCourse(String stuId, String courseCode);

    // Insert a new mark record
    void addMark(Mark mark);

    // Update an existing mark record
    void updateMark(Mark mark);

    // Delete a mark record
    void deleteMark(String markId);

    // Check if a student has medical excuse for a specific exam type
    boolean hasMedicalForExam(String stuId, String courseCode, String examTypeId);

}
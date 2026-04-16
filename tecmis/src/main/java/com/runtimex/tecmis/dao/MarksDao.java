package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Mark;
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

}
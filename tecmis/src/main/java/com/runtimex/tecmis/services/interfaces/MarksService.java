package com.runtimex.tecmis.services.interfaces;

import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseResultSummary;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.StudentGpaSummary;
import com.runtimex.tecmis.models.StudentInfo;
import java.util.List;

public interface MarksService {
    double calculateFinalMark(String stuId, String courseCode);
    double calculateTopTwoQuizzes(List<Double> quizMarks);

    List<CourseExam> getCourseExams(String courseCode);

    List<CourseUnit> getAllCourses();

    List<CourseUnit> getCoursesByStudent(String stuId);

    List<StudentInfo> getStudentsByCourse(String courseCode);

    CourseResultSummary getCourseResult(String stuId, String courseCode, boolean includeMedicalAttendance);

    List<CourseResultSummary> getCourseResultsForBatch(String courseCode, boolean includeMedicalAttendance);

    StudentGpaSummary getStudentGpaSummary(String stuId, boolean includeMedicalAttendance);

    List<StudentGpaSummary> getStudentGpaSummariesForCourse(String courseCode, boolean includeMedicalAttendance);
}

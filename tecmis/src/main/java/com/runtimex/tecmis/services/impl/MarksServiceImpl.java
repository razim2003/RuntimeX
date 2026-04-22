package com.runtimex.tecmis.services.impl;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.models.AttendanceSummary;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseResultSummary;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.MarkEntry;
import com.runtimex.tecmis.models.StudentGpaSummary;
import com.runtimex.tecmis.models.StudentInfo;
import com.runtimex.tecmis.services.interfaces.MarksService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarksServiceImpl implements MarksService {

    private static final String STATUS_SUSPENDED = "Suspended";

    private final MarksDao marksDao;
    private final AttendanceDao attendanceDao;

    public MarksServiceImpl(MarksDao marksDao, AttendanceDao attendanceDao) {
        this.marksDao = marksDao;
        this.attendanceDao = attendanceDao;
    }

    @Override
    public double calculateFinalMark(String stuId, String courseCode) {
        CourseResultSummary summary = getCourseResult(stuId, courseCode, true);
        return summary == null ? 0.0 : summary.getTotalMark();
    }

    @Override
    public double calculateTopTwoQuizzes(List<Double> quizMarks) {
        if (quizMarks == null || quizMarks.isEmpty()) {
            return 0.0;
        }
        return quizMarks.stream()
                .filter(m -> m != null)
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    public List<CourseExam> getCourseExams(String courseCode) {
        return marksDao.getCourseExams(courseCode);
    }

    @Override
    public List<CourseUnit> getAllCourses() {
        return marksDao.getAllCourses();
    }

    @Override
    public List<CourseUnit> getCoursesByStudent(String stuId) {
        return marksDao.getCoursesByStudent(stuId);
    }

    @Override
    public List<StudentInfo> getStudentsByCourse(String courseCode) {
        return marksDao.getStudentsByCourse(courseCode);
    }

    @Override
    public CourseResultSummary getCourseResult(String stuId, String courseCode, boolean includeMedicalAttendance) {
        List<StudentInfo> students = new ArrayList<>();
        students.add(new StudentInfo(stuId, "", marksDao.getUndergraduateStatus(stuId)));
        Map<String, AttendanceSummary> attendanceByStudent = buildAttendanceMap(
                courseCode, includeMedicalAttendance);

        return buildCourseResult(
                students.get(0),
                courseCode,
                attendanceByStudent.get(stuId));
    }

    @Override
    public List<CourseResultSummary> getCourseResultsForBatch(String courseCode,
            boolean includeMedicalAttendance) {
        List<StudentInfo> students = marksDao.getStudentsByCourse(courseCode);
        Map<String, AttendanceSummary> attendanceByStudent = buildAttendanceMap(
                courseCode, includeMedicalAttendance);

        List<CourseResultSummary> results = new ArrayList<>();
        for (StudentInfo student : students) {
            results.add(buildCourseResult(
                    student,
                    courseCode,
                    attendanceByStudent.get(student.getStudentId())));
        }

        return results;
    }

    @Override
    public StudentGpaSummary getStudentGpaSummary(String stuId, boolean includeMedicalAttendance) {
        String studentName = "";
        List<CourseUnit> courses = marksDao.getCoursesByStudent(stuId);
        double totalPoints = 0.0;
        int totalCredits = 0;

        for (CourseUnit course : courses) {
            CourseResultSummary result = getCourseResult(stuId, course.getCourseCode(), includeMedicalAttendance);
            if (studentName.isBlank()) {
                studentName = result.getStudentName() == null ? "" : result.getStudentName();
            }
            double points = gradePoint(result.getGrade());
            totalPoints += points * course.getCredit();
            totalCredits += course.getCredit();
        }

        double sgpa = totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
        return new StudentGpaSummary(stuId, studentName, sgpa, sgpa);
    }

    @Override
    public List<StudentGpaSummary> getStudentGpaSummariesForCourse(String courseCode,
            boolean includeMedicalAttendance) {
        List<StudentInfo> students = marksDao.getStudentsByCourse(courseCode);
        List<StudentGpaSummary> summaries = new ArrayList<>();
        for (StudentInfo student : students) {
            StudentGpaSummary summary = getStudentGpaSummary(student.getStudentId(), includeMedicalAttendance);
            if (summary.getStudentName() == null || summary.getStudentName().isBlank()) {
                summary.setStudentName(student.getStudentName());
            }
            summaries.add(summary);
        }
        return summaries;
    }

    private Map<String, AttendanceSummary> buildAttendanceMap(String courseCode,
            boolean includeMedicalAttendance) {
        Map<String, AttendanceSummary> map = new HashMap<>();
        List<AttendanceSummary> summaries = attendanceDao.getAttendanceSummaryByCourse(
                courseCode, "Combined", includeMedicalAttendance);
        for (AttendanceSummary summary : summaries) {
            map.put(summary.getStudentId(), summary);
        }
        return map;
    }

    private CourseResultSummary buildCourseResult(StudentInfo student, String courseCode,
            AttendanceSummary attendanceSummary) {
        CourseResultSummary summary = new CourseResultSummary();
        summary.setStudentId(student.getStudentId());
        summary.setStudentName(student.getStudentName());
        summary.setCourseCode(courseCode);
        summary.setStudentStatus(student.getStatus());

        List<CourseExam> exams = marksDao.getCourseExams(courseCode);
        List<MarkEntry> entries = marksDao.getMarkEntries(student.getStudentId(), courseCode);

        Map<String, Double> marksByType = new HashMap<>();
        for (MarkEntry entry : entries) {
            marksByType.put(entry.getExamTypeId(), entry.getMark());
        }

        List<String> quizTypes = List.of("QU01", "QU02", "QU03");
        List<String> caTypes = List.of("ASST", "MID");
        List<String> finalTypes = List.of("FIN");

        double caWeightedTotal = 0.0;
        double caWeightSum = 0.0;

        List<CourseExam> quizExams = new ArrayList<>();
        for (CourseExam exam : exams) {
            if (quizTypes.contains(exam.getExamTypeId())) {
                quizExams.add(exam);
            }
        }

        Comparator<CourseExam> quizComparator = Comparator.comparingDouble((CourseExam exam) -> {
            Double value = marksByType.get(exam.getExamTypeId());
            return value == null ? -1 : value;
        });
        quizExams.sort(quizComparator.reversed());

        int quizCount = 0;
        for (CourseExam exam : quizExams) {
            if (quizCount >= 2) {
                break;
            }
            Double mark = marksByType.get(exam.getExamTypeId());
            double value = mark == null ? 0.0 : mark;
            caWeightedTotal += value * exam.getWeight();
            caWeightSum += exam.getWeight();
            quizCount++;
        }

        for (CourseExam exam : exams) {
            if (caTypes.contains(exam.getExamTypeId())) {
                Double mark = marksByType.get(exam.getExamTypeId());
                double value = mark == null ? 0.0 : mark;
                caWeightedTotal += value * exam.getWeight();
                caWeightSum += exam.getWeight();
            }
        }

        double caPercentage = caWeightSum == 0 ? 0.0 : caWeightedTotal / caWeightSum;

        double endWeightedTotal = 0.0;
        double endWeightSum = 0.0;
        boolean hasFinalExam = false;
        boolean finalMissingWithMedical = false;

        for (CourseExam exam : exams) {
            if (finalTypes.contains(exam.getExamTypeId())) {
                hasFinalExam = true;
                Double mark = marksByType.get(exam.getExamTypeId());
                double value = mark == null ? 0.0 : mark;
                endWeightedTotal += value * exam.getWeight();
                endWeightSum += exam.getWeight();

                if (mark == null && marksDao.hasApprovedExamMedical(
                        student.getStudentId(), courseCode, exam.getExamTypeId())) {
                    finalMissingWithMedical = true;
                }
            }
        }

        double endPercentage = endWeightSum == 0 ? 0.0 : endWeightedTotal / endWeightSum;
        double totalMark = caWeightedTotal + endWeightedTotal;

        summary.setCaPercentage(caPercentage);
        summary.setEndPercentage(endPercentage);
        summary.setTotalMark(totalMark);
        boolean hasCourseMedical = marksDao.hasExamMedicalForCourse(student.getStudentId(), courseCode);
        summary.setMedicalConcession(finalMissingWithMedical || hasCourseMedical);

        double attendancePercentage = attendanceSummary == null ? 0.0 : attendanceSummary.getPercentage();
        summary.setAttendancePercentage(attendancePercentage);

        boolean caPass = caPercentage >= 40.0;
        boolean endPass = endPercentage >= 40.0;
        boolean attendanceEligible = attendancePercentage >= 80.0;

        summary.setCaStatus(caPass ? "Pass" : "Fail");

        if (STATUS_SUSPENDED.equalsIgnoreCase(student.getStatus())) {
            summary.setEligibilityStatus("Withheld");
            summary.setEndStatus("WH");
            summary.setGrade("WH");
            return summary;
        }

        if (!attendanceEligible) {
            summary.setEligibilityStatus("Not Eligible");
            summary.setEndStatus("EE (Not Eligible)");
            summary.setGrade("EE");
            return summary;
        }

        summary.setEligibilityStatus(caPass ? "Eligible" : "Not Eligible");

        if (finalMissingWithMedical && hasFinalExam) {
            summary.setEndStatus("MC");
            summary.setGrade("MC");
            return summary;
        }

        if (!caPass && !endPass && hasFinalExam) {
            summary.setEndStatus("E");
            summary.setGrade("E");
            return summary;
        }

        if (!caPass) {
            summary.setEndStatus("EC");
            summary.setGrade("EC");
            return summary;
        }

        if (hasFinalExam && !endPass) {
            summary.setEndStatus("EE");
            summary.setGrade("EE");
            return summary;
        }

        summary.setEndStatus(hasFinalExam ? "Pass" : "-" );
        summary.setGrade(toGrade(totalMark));
        return summary;
    }

    private String toGrade(double totalMark) {
        if (totalMark >= 85.0) {
            return "A+";
        }
        if (totalMark >= 75.0) {
            return "A";
        }
        if (totalMark >= 70.0) {
            return "A-";
        }
        if (totalMark >= 65.0) {
            return "B+";
        }
        if (totalMark >= 60.0) {
            return "B";
        }
        if (totalMark >= 55.0) {
            return "B-";
        }
        if (totalMark >= 50.0) {
            return "C+";
        }
        if (totalMark >= 45.0) {
            return "C";
        }
        if (totalMark >= 40.0) {
            return "C-";
        }
        if (totalMark >= 35.0) {
            return "D";
        }
        return "E";
    }

    private double gradePoint(String grade) {
        if (grade == null) {
            return 0.0;
        }
        return switch (grade) {
            case "A+", "A" -> 4.00;
            case "A-" -> 3.70;
            case "B+" -> 3.30;
            case "B" -> 3.00;
            case "B-" -> 2.70;
            case "C+" -> 2.30;
            case "C" -> 2.00;
            case "C-" -> 1.70;
            case "D" -> 1.30;
            default -> 0.0;
        };
    }
}

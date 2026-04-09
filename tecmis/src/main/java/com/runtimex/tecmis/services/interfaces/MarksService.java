package com.runtimex.tecmis.services.interfaces;

import java.util.List;

public interface MarksService {
    double calculateFinalMark(String stuId, String courseCode);
    double calculateTopTwoQuizzes(List<Double> quizMarks);
}

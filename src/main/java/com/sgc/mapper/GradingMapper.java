package com.sgc.mapper;


import com.sgc.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradingMapper {

    List<String> getStudentIdsByExamId(Integer examId);

    List<Student> getStudentsByIds(List<String> studentIds);

    List<String> getQuestionIdsByExamIdAndStudentId(String examId, String studentId);

    String getQuestionById(String id);

    void submitGrading(String examId, String studentId, String questionId, Double score);

    Double getScoreByExamIdAndStudentIdAndQuestionId(String examId, String studentId, String questionId);

    List<String> getScoresByExamIdAndStudentId(String examId, String studentId);

    void updateGradingTimeAndTotalScoreByStudentIdAndExamId(String studentId, String examId, Double totalScore);
}

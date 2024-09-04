package com.sgc.mapper;

import com.sgc.entity.*;
import com.sgc.vo.CustomerQuestionReturnVo;

import java.util.List;

public interface CustomerAppMapper {
    List<Exam> findAll(String studentId);

//    String findStudentNameById(String studentId);

    List<Integer> getQuestionIdsByStudentIdAndExamId(String studentId, String examId);

    List<Question> getQuestionsByIds(List<Integer> questionIds);

    Exam getExamInfoById(String examId);

    ExamQuestion getNowQuestionInfoById(String examId, String studentId, String questionId);

    String getNowQuestionAnswerByExamIdAndStudentIdAndQuestionId(String examId, String studentId, String questionId);

    boolean saveNowQuestion(CustomerQuestionReturnVo question);

    boolean submitExam(String examId, String studentId);


    List<ExamGrading> findAbleToSubmit(String examId, String studentId);

    String getSubmissionTime(String studentId, String examId);

    Student findStudentById(String studentId);

    String getQuestionContent(String questionId);

    String getNowQuestionImgByExamIdAndStudentIdAndQuestionId(String examId, String studentId, String questionId);
}

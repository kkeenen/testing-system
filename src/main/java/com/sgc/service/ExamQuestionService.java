package com.sgc.service;

import com.sgc.entity.Exam;
import com.sgc.entity.ExamQuestion;
import com.sgc.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ExamQuestionService {
    List<ExamQuestion> findAll();

    List<ExamQuestion> findByCondition(ExamQuestion examQuestion);

    boolean sendQuestionToStudentByClassName(Map<String, Object> map);

    List<List<String>> toGetQuestions(Integer examId);

    Integer getCountOfQuestionsByExamId(Integer id);

    boolean sendExam(Integer examId);


}

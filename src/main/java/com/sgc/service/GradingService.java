package com.sgc.service;

import com.sgc.vo.AnsweredQuestionVo;
import com.sgc.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GradingService {
    List<Student> getStudentsByExamId(Integer examId);

    Boolean getStatus(String examId, String studentId);

    List<AnsweredQuestionVo> getStudentQuestions(String examId, String studentId);

    boolean submitGrading(List<AnsweredQuestionVo> answeredQuestionVo);

    Boolean getIsGraded(String examId, String studentId);
}

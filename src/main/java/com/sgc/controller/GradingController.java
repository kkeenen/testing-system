package com.sgc.controller;

import com.sgc.vo.AnsweredQuestionVo;
import com.sgc.entity.Exam;
import com.sgc.entity.Student;
import com.sgc.mapper.ExamMapper;
import com.sgc.service.GradingService;
import com.sgc.vo.GradedStudentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/grading/")
public class GradingController {

    @Autowired
    private GradingService gradingService;
    @Autowired
    private ExamMapper examMapper;

    @GetMapping("getStudentsByExamId")
    private ResponseEntity<Map<String, Object>> getStudentsByExamId(Integer examId) {
        List<Student> list = gradingService.getStudentsByExamId(examId);

        List<GradedStudentVo> gradedStudentVos = list.stream().map(item -> {
            GradedStudentVo gradedStudentVo = new GradedStudentVo();
            BeanUtils.copyProperties(item, gradedStudentVo);
            gradedStudentVo.setStatus(gradingService.getStatus(examId.toString(), item.getStudentId()));
            gradedStudentVo.setIsGraded(gradingService.getIsGraded(examId.toString(),item.getStudentId()));
            return gradedStudentVo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("success", true, "data", gradedStudentVos));
    }

    @GetMapping("getExamInfo")
    private ResponseEntity<Map<String, Object>> getExamInfo(Integer examId) {
        Exam exam = examMapper.getById(examId);
        return ResponseEntity.ok(Map.of("success", true, "data", exam));
    }

    @GetMapping("getStudentQuestions")
    private ResponseEntity<Map<String, Object>> getStudentQuestions(String examId, String studentId) {
        List<AnsweredQuestionVo> list = gradingService.getStudentQuestions(examId, studentId);
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @PostMapping("submitGrading")
    private ResponseEntity<Map<String, Object>> submitGrading(@RequestBody List<AnsweredQuestionVo> answeredQuestionVo) {
        boolean flg = gradingService.submitGrading(answeredQuestionVo);

        return ResponseEntity.ok(Map.of("success", flg));
    }

}

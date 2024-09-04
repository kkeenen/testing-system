package com.sgc.controller;

import com.sgc.entity.Exam;
import com.sgc.entity.ExamQuestion;
import com.sgc.entity.Question;
import com.sgc.service.ExamQuestionService;
import com.sgc.service.QuestionService;
import com.sgc.utils.MyTools;
import com.sgc.utils.PageResult;
import com.sgc.vo.EditQuestionVo;
import com.sgc.vo.QuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/")
public class ExamQuestionController {

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/examQuestion/list")
    private ResponseEntity<Map<String, Object>> list(ExamQuestion examQuestion) {

        List<ExamQuestion> list = null;
        if (StringUtils.isEmpty(examQuestion.getExamId()) && StringUtils.isEmpty(examQuestion.getStudentId()) &&
                StringUtils.isEmpty(examQuestion.getQuestionId()) && StringUtils.isEmpty(examQuestion.getScore()) &&
                StringUtils.isEmpty(examQuestion.getContent())) {
            list = examQuestionService.findAll();
        } else {
            list = examQuestionService.findByCondition(examQuestion);
        }

        //封装vo
        List<QuestionVo> collect = list.stream().map(item -> {
            QuestionVo questionVo = new QuestionVo();
            questionVo.setQuestionId(item.getQuestionId());
            questionVo.setContent(questionService.getContentByQuestionId(item.getQuestionId()));
            return questionVo;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("success", true, "records", collect));
    }

    @GetMapping("/examQuestion/findAllOptions")
    private ResponseEntity<Map<String, Object>> findAllOptions() {

        // 查找所有question
        List<Question> all = questionService.findAllWithoutPage();

        // 得到option
        List<EditQuestionVo> options = questionService.getOptions(all);

        // 返回map
        return ResponseEntity.ok(Map.of("success", true, "options", options));
    }

    @PostMapping("/examQuestion/sendQuestionToStudentByClassName")
    private ResponseEntity<Map<String, Object>> sendQuestionToStudentByClassName(@RequestBody Map<String, Object> map) {

        boolean flg = examQuestionService.sendQuestionToStudentByClassName(map);
        return ResponseEntity.ok(Map.of("success", flg));
    }

    @GetMapping("/examQuestion/toGetQuestions")
    private ResponseEntity<Map<String, Object>> toGetQuestions(Integer examId) {
        List<List<String>> list = examQuestionService.toGetQuestions(examId);
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @GetMapping("examQuestion/sendExam")
    private ResponseEntity<Map<String, Object>> sendExam(Integer examId) {
        boolean flg = examQuestionService.sendExam(examId);
        return ResponseEntity.ok(Map.of("success", true));
    }



}

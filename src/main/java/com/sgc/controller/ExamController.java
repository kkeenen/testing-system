package com.sgc.controller;

import com.sgc.entity.Exam;
import com.sgc.mapper.StudentMapper;
import com.sgc.service.ExamQuestionService;
import com.sgc.service.ExamService;
import com.sgc.utils.PageResult;
import com.sgc.vo.EchartsVo;
import com.sgc.vo.ExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ExamQuestionService examQuestionService;


    @GetMapping("/exam/list")
    private ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     Exam exam) {

        Map<String, Object> map = examService.list(pageNo, pageSize, exam);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/exam/add")
    private ResponseEntity<Map<String, Object>> add(@RequestBody Exam exam) {

        int i = examService.add(exam);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }
    @PostMapping("/exam/edit")
    private ResponseEntity<Map<String, Object>> edit(@RequestBody Exam exam) {

        int i = examService.edit(exam);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }
    @PostMapping("/exam/delete")
    private ResponseEntity<Map<String, Object>> delete(@RequestBody Integer[] ids) {
        int i = examService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }

    @GetMapping("/exam/findAllClasses")
    private ResponseEntity<Map<String, Object>> findAllClasses() {
        List<String> list = studentMapper.findClasses();
        return ResponseEntity.ok(Map.of("success",true, "data", list));
    }

    @GetMapping("echarts/getAllMajorQuestions")
    private ResponseEntity<Map<String, Object>> getAllMajorQuestions() {
        List<EchartsVo> list = examService.getAllMajorQuestions();
        return ResponseEntity.ok(Map.of("success",true, "data", list));
    }

    @GetMapping("echarts/getCreatedExamAndSentExam")
    private ResponseEntity<Map<String, Object>> getCreatedExamAndSentExam() {
        List<EchartsVo> list = examService.getCreatedExamAndSentExam();
        return ResponseEntity.ok(Map.of("success",true, "data", list));
    }

    @GetMapping("echarts/getExamCountOfClass")
    private ResponseEntity<Map<String, Object>> getExamCountOfClass(){
        Map<String, Object> map = examService.getExamCountOfClass();

        List<EchartsVo> voList = (List<EchartsVo>) map.get("data");
        List<String> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (EchartsVo echartsVo : voList) {
            x.add(echartsVo.getName());
            y.add(echartsVo.getValue());
        }

        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("x", x, "y", y)));
    }

}

package com.sgc.controller;

import com.sgc.entity.Question;
import com.sgc.service.QuestionService;
import com.sgc.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/list")
    private ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     Question question) {
        PageResult<Question> list = null;
        if( StringUtils.isEmpty(question.getId())               && StringUtils.isEmpty(question.getContent()) &&
            StringUtils.isEmpty(question.getReferenceAnswer())  && StringUtils.isEmpty(question.getField()) ){
            list = questionService.findAll(pageNo, pageSize);

        }else{
            list = questionService.findByCondition(pageNo, pageSize, question);

        }

        return ResponseEntity.ok(Map.of("success",true, "records", list.getData(), "total", list.getTotal()));
    }
    @GetMapping("/question/findFields")
    private ResponseEntity<Map<String, Object>> findFields() {
        List<String> list =  questionService.findFields();
        return ResponseEntity.ok(Map.of("success",true, "data", list));
    }

    @PostMapping("/question/add")
    private ResponseEntity<Map<String, Object>> add(@RequestBody Question question) {
        int i = questionService.add(question);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }
    @PostMapping("/question/edit")
    private ResponseEntity<Map<String, Object>> edit(@RequestBody Question question) {
        int i = questionService.edit(question);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }
    @PostMapping("/question/delete")
    private ResponseEntity<Map<String, Object>> delete(@RequestBody Integer[] ids) {
        int i = questionService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("success",true, "rows", i));
    }

}

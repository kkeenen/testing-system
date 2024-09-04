package com.sgc.controller;


import com.sgc.utils.PageResult;
import com.sgc.entity.Student;
import com.sgc.service.StudentService;
import com.sgc.vo.EchartsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 查询学生
    @GetMapping("/student/list")
    private ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     Student student){
        PageResult<Student> list = null;
        if(StringUtils.isEmpty(student.getStudentId())&&StringUtils.isEmpty(student.getName())&& StringUtils.isEmpty(student.getClassName()) &&StringUtils.isEmpty(student.getGender())&& StringUtils.isEmpty(student.getPhone())     &&StringUtils.isEmpty(student.getQq())&& StringUtils.isEmpty(student.getMajor())) {
            list = studentService.findAll(pageNo, pageSize);
        }else{
            list = studentService.findByCondition(pageNo, pageSize, student);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", list.getTotal());
        map.put("records", list.getData());
        map.put("success", true);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/student/findClasses")
    private ResponseEntity<Map<String,Object>> findClasses(){
        List<String> list = studentService.findClasses();
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @PostMapping("/student/add")
    private ResponseEntity<Map<String, Object>> add(@RequestBody Student student){

        int rows = studentService.add(student);
        return ResponseEntity.ok(Map.of("success", true, "rows", rows));
    }

    @PostMapping("/student/edit")
    private ResponseEntity<Map<String, Object>> edit(@RequestBody Student student){
        int rows = studentService.edit(student);
        return ResponseEntity.ok(Map.of("success", true, "rows", rows));
    }

    @PostMapping("/student/delete")
    private ResponseEntity<Map<String, Object>> deleteByIds(@RequestBody Integer[] ids){
        int rows = studentService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("success", true, "rows", rows));
    }

    @GetMapping("/echarts/getGenderData")
    private ResponseEntity<Map<String, Object>> getGenderData(){
        List<EchartsVo> list = studentService.getGenderData();
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }
    @GetMapping("/echarts/getMajorData")
    private ResponseEntity<Map<String, Object>> getMajorData(){
        List<EchartsVo> list = studentService.getMajorData();
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @GetMapping("echarts/getEntryTrend")
    private ResponseEntity<Map<String, Object>> getEntryTrend(){
        Map<String, Object> map = studentService.getEntryTrend();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        Map<String, Integer> dateCountMap = (Map<String, Integer>) map.get("data");
        for (Map.Entry<String, Integer> entry : dateCountMap.entrySet()) {
            dates.add(entry.getKey());
            counts.add(entry.getValue());
        }
        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("date", dates, "count", counts)));
    }



}

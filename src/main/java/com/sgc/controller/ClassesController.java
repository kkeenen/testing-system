package com.sgc.controller;

import com.sgc.entity.Classes;
import com.sgc.utils.PageResult;
import com.sgc.mapper.ClassesMapper;
import com.sgc.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ClassesController {

    @Autowired
    private ClassesService classesService;
    @Autowired
    private ClassesMapper classesMapper;

    @GetMapping("/class/list")
    private ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     Classes classes) {
        PageResult<Classes> result = null;


        if( StringUtils.isEmpty(classes.getId())        &&  StringUtils.isEmpty(classes.getClassId()) &&
            StringUtils.isEmpty(classes.getClassName()) && StringUtils.isEmpty(classes.getStartDate())&&
            StringUtils.isEmpty(classes.getEndDate())   ){
            result = classesService.findAll(pageNo, pageSize);
        }else{
            result = classesService.findByCondition(pageNo, pageSize, classes);
        }
        // 查每个班级的学生数
        result.getData().forEach(item->{
            item.setNumberOfStudents(getStudentNumber(item.getClassName()));
        });
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("records", result.getData());
        map.put("success", true);
        return ResponseEntity.ok(map);
    }

    private int getStudentNumber(String className){
        int count = classesMapper.getStudentNumber(className);
        return count;
    }

    @PostMapping("/class/add")
    private ResponseEntity<Map<String, Object>> add(@RequestBody Classes classes) {
        int i = classesService.add(classes);
        return ResponseEntity.ok(Map.of("success", true,  "rows", i));
    }

    @PostMapping("/class/edit")
    private ResponseEntity<Map<String, Object>> edit(@RequestBody Classes classes) {
        int i = classesService.edit(classes);
        return ResponseEntity.ok(Map.of("success", true,  "rows", i));
    }

    @PostMapping("/class/delete")
    private ResponseEntity<Map<String, Object>> delete(@RequestBody Integer[] ids) {
        int i = classesService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("success", true,  "rows", i));
    }





}

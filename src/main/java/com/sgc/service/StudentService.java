package com.sgc.service;

import com.sgc.utils.PageResult;
import com.sgc.entity.Student;
import com.sgc.vo.EchartsVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface StudentService {

    PageResult<Student> findAll(Integer pageNo, Integer pageSize);

    List<String> findClasses();

    PageResult<Student> findByCondition(Integer pageNo, Integer pageSize, Student student);

    int add(Student student);

    int edit(Student student);

    int deleteByIds(Integer[] ids);

    List<Student> findAllStudentsByClassName(List<String> classNameList);

    List<EchartsVo> getGenderData();

    List<EchartsVo> getMajorData();

    Map<String, Object> getEntryTrend();


}

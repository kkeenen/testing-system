package com.sgc.service;

import com.sgc.entity.Exam;
import com.sgc.utils.PageResult;
import com.sgc.vo.EchartsVo;
import com.sgc.vo.ExamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ExamService {

    PageResult<Exam> findAll(@Param("pageNo") Integer pageNo,
                             @Param("pageSize")Integer pageSize);

    PageResult<Exam> findByCondition(@Param("pageNo")Integer pageNo,
                                     @Param("pageSize")Integer pageSize,
                                     @Param("exam")Exam exam);

    int add(Exam exam);

    int edit(Exam exam);

    int deleteByIds(Integer[] ids);


    String dealStatus(String status);

    Map<String, Object> list(Integer pageNo, Integer pageSize, Exam exam);

    List<EchartsVo> getAllMajorQuestions();

    List<EchartsVo> getCreatedExamAndSentExam();

    Map<String, Object> getExamCountOfClass();

}

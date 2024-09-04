package com.sgc.mapper;

import com.sgc.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamMapper {
    List<Exam> findAll(Integer pageSize, int offset);

    long findAllCount(Integer pageSize, int offset);

    List<Exam> findByCondition(@Param("pageSize")Integer pageSize,
                               @Param("offset")int offset,
                               @Param("exam")Exam exam);

    long findByConditionCount(@Param("pageSize")Integer pageSize,
                              @Param("offset")int offset,
                              @Param("exam")Exam exam);

    int add(Exam exam);

    int edit(Exam exam);

    int deleteByIds(Integer[] ids);


    int sendExam(Integer examId);

    Exam getById(Integer examId);

    Integer findCountOfStudentsByExamId(String examId);

    Integer findCountOfSubmitStudentsByExamId(String examId);

    Integer findCountOfGradedStudentsByExamId(String examId);

    int getCountOfSentExam();

    Integer findCountByClassName(String className);

}

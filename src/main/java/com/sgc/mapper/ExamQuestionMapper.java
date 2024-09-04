package com.sgc.mapper;

import com.sgc.entity.Exam;
import com.sgc.entity.ExamQuestion;
import com.sgc.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamQuestionMapper {

    List<ExamQuestion> findAll();

    long findAllCount();

    long findByConditionCount(@Param("examQuestion")ExamQuestion examQuestion);


    List<ExamQuestion> findByCondition(@Param("examQuestion")ExamQuestion examQuestion);

    void setExamAndStudentAndQuestions(int examId, String studentId, Integer questionId);


    List<Integer> toGetQuestionIdsByExamId(Integer examId);

    Integer getCountOfQuestionsByExamId(Integer id);


    Integer deleteOriginData(Integer examId, List<String> studentIds);
}

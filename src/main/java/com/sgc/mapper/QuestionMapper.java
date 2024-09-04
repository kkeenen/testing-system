package com.sgc.mapper;

import com.sgc.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    List<Question> findAll(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    List<String> findFields();

    long findAllCount();

    List<Question> findByCondition(@Param("pageSize")Integer pageSize,
                                   @Param("offset")Integer offset,
                                   @Param("question")Question question);

    long findByConditionCount(@Param("question") Question question);

    int add(Question question);

    int edit(Question question);

    int deleteByIds(Integer[] ids);

    String getContentByQuestionId(String questionId);

    List<Question> findAllWithoutPage();


    String findFieldsById(Integer questionId);
}

package com.sgc.service;

import com.sgc.entity.Question;
import com.sgc.utils.PageResult;
import com.sgc.vo.EditQuestionVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    PageResult<Question> findAll(Integer pageNo, Integer pageSize);

    List<String> findFields();

    PageResult<Question> findByCondition(Integer pageNo, Integer pageSize, Question question);

    int add(Question question);

    int edit(Question question);

    int deleteByIds(Integer[] ids);

    String getContentByQuestionId(String questionId);

    List<Question> findAllWithoutPage();

    List<EditQuestionVo> getOptions(List<Question> all);


}

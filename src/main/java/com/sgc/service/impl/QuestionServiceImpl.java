package com.sgc.service.impl;

import com.sgc.entity.Question;
import com.sgc.mapper.QuestionMapper;
import com.sgc.service.QuestionService;
import com.sgc.utils.PageResult;
import com.sgc.vo.EditQuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public PageResult<Question> findAll(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Question> list = questionMapper.findAll(pageSize, offset);
        PageResult<Question> page = new PageResult<>();
        page.setData(list);
        page.setTotal(questionMapper.findAllCount());
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public List<String> findFields() {
        List<String> list = questionMapper.findFields();
        return list;
    }

    @Override
    public PageResult<Question> findByCondition(Integer pageNo, Integer pageSize, Question question) {
        int offset = (pageNo - 1) * pageSize;
        List<Question> list = questionMapper.findByCondition(pageSize, offset, question);
        PageResult<Question> page = new PageResult<>();
        page.setData(list);
        page.setTotal(questionMapper.findByConditionCount(question));
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;

    }

    @Override
    public int add(Question question) {
        int i = questionMapper.add(question);
        return i;
    }

    @Override
    public int edit(Question question) {
        int i = questionMapper.edit(question);
        return i;
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        int i = questionMapper.deleteByIds(ids);
        return i;
    }

    @Override
    public String getContentByQuestionId(String questionId) {
        String content = questionMapper.getContentByQuestionId(questionId);
        return content;
    }

    @Override
    public List<Question> findAllWithoutPage() {
        List<Question> list = questionMapper.findAllWithoutPage();
        return list;
    }

    @Override
    public List<EditQuestionVo> getOptions(List<Question> all) {
        // 找到所有专业
        List<String> fields = all.stream().map(item -> {
            String field = item.getField();
            return field;
        }).distinct().collect(Collectors.toList());

        List<EditQuestionVo> options = fields.stream().map(item -> {
            EditQuestionVo editQuestionVo = new EditQuestionVo();
            editQuestionVo.setLabel(item);
            editQuestionVo.setValue(item);

            List<EditQuestionVo> children = all.stream()
                    .filter(question -> question.getField().equalsIgnoreCase(item))
                    .map(question -> {
                        EditQuestionVo child = new EditQuestionVo();
                        if (question.getField().equalsIgnoreCase(item)) {
                            child.setLabel(question.getContent());
                            child.setValue(question.getId().toString());
                        }
                        return child;
                    }).collect(Collectors.toList());
            editQuestionVo.setChildren(children);
            return editQuestionVo;
        }).collect(Collectors.toList());

        return options;
    }


}

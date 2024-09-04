package com.sgc.service.impl;

import com.sgc.entity.Classes;
import com.sgc.entity.Exam;
import com.sgc.entity.Question;
import com.sgc.mapper.ClassesMapper;
import com.sgc.mapper.ExamMapper;
import com.sgc.mapper.QuestionMapper;
import com.sgc.service.ExamQuestionService;
import com.sgc.service.ExamService;
import com.sgc.utils.PageResult;
import com.sgc.vo.EchartsVo;
import com.sgc.vo.ExamVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamQuestionService examQuestionService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ClassesMapper classesMapper;

    @Override
    public Map<String, Object> list(Integer pageNo, Integer pageSize, Exam exam) {
        PageResult<Exam> exams = null;
        if(     StringUtils.isEmpty(exam.getId())          && StringUtils.isEmpty(exam.getTitle())      &&
                StringUtils.isEmpty(exam.getFacedClass())  && StringUtils.isEmpty(exam.getStartTime())  &&
                StringUtils.isEmpty(exam.getEndTime())     && StringUtils.isEmpty(exam.getStatus()) ){

            exams = findAll(pageNo, pageSize);

        }else{

            exams = findByCondition(pageNo, pageSize, exam);
        }

        List<ExamVo> examVos = exams.getData().stream().map(item -> {
            // 根据试卷id查找试题数
            item.setCountOfQuestions(examQuestionService.getCountOfQuestionsByExamId(item.getId()));
            // 设置status字段
            item.setStatus(dealStatus(item.getStatus()));
            ExamVo examVo = new ExamVo();
            BeanUtils.copyProperties(item, examVo);
            examVo.setStudentCount(examMapper.findCountOfStudentsByExamId(item.getId().toString()));
            examVo.setSubmitCount(examMapper.findCountOfSubmitStudentsByExamId(item.getId().toString()));
            examVo.setGradeCount(examMapper.findCountOfGradedStudentsByExamId(item.getId().toString()));

            return examVo;
        }).collect(Collectors.toList());
        return Map.of("success", true, "records", examVos, "total", exams.getTotal());
    }


    @Override
    public PageResult<Exam> findAll(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Exam> list = examMapper.findAll(pageSize, offset);
        PageResult<Exam> page = new PageResult<>();
        page.setData(list);
        page.setTotal(examMapper.findAllCount(pageSize, offset));
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public PageResult<Exam> findByCondition(Integer pageNo, Integer pageSize, Exam exam) {
        int offset = (pageNo - 1) * pageSize;

        // 反转status ： 待发放-》1  ； 已发放-》2
        if(!StringUtils.isEmpty(exam.getStatus())){
            if(exam.getStatus().equalsIgnoreCase("待发放")){
                exam.setStatus("1");
            }else{
                exam.setStatus("2");
            }
        }

        List<Exam> list = examMapper.findByCondition(pageSize, offset, exam);

        PageResult<Exam> page = new PageResult<>();
        page.setData(list);
        page.setTotal(examMapper.findByConditionCount(pageSize, offset, exam));
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public int add(Exam exam) {
        int i = examMapper.add(exam);
        return i;
    }

    @Override
    public int edit(Exam exam) {
        int i = examMapper.edit(exam);
        return i;
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        int i = examMapper.deleteByIds(ids);
        return i;
    }

    @Override
    public String dealStatus(String status) {
        if(status.equalsIgnoreCase("1")){
            status = "待发放";
        }else if(status.equalsIgnoreCase("2")){
            status = "已发放";
        }else {
            status = "异常";
        }
        return status;
    }

    @Override
    public List<EchartsVo> getAllMajorQuestions() {
        List<Question> questionList = questionMapper.findAll(Integer.MAX_VALUE, 0);
        List<String> fields = questionList.stream().map(Question::getField).distinct().collect(Collectors.toList());


        List<EchartsVo> voList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        for (String field : fields) {
            int cnt = 0;
            for(Question question : questionList){
                if(question.getField().equals(field)){
                    cnt ++;
                }
            }
            countList.add(cnt);
            EchartsVo echartsVo = new EchartsVo(field, cnt);
            voList.add(echartsVo);
        }

        return voList;
    }

    @Override
    public List<EchartsVo> getCreatedExamAndSentExam() {

        int allCount = (int)examMapper.findAllCount(Integer.MAX_VALUE, 0);

        int sentCount = examMapper.getCountOfSentExam();

        List<EchartsVo> voList = new ArrayList<>();
        voList.add(new EchartsVo("未下发", allCount - sentCount));
        voList.add(new EchartsVo("已下发", sentCount));
        return voList;
    }

    @Override
    public Map<String, Object> getExamCountOfClass() {

        // 查找所有班级
        List<Classes> allClasses = classesMapper.findAll(Integer.MAX_VALUE, 0);

        List<EchartsVo> voList = new ArrayList<>();
        // 查找每个班级的考试数
        allClasses.forEach(classes -> {

            Integer count = examMapper.findCountByClassName(classes.getClassName());
            voList.add(new EchartsVo(classes.getClassName(), count));

        });
        return Map.of("data", voList);

    }


}

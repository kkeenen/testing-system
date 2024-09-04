package com.sgc.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.sgc.entity.Exam;
import com.sgc.entity.ExamQuestion;
import com.sgc.entity.Question;
import com.sgc.entity.Student;
import com.sgc.mapper.ExamMapper;
import com.sgc.mapper.ExamQuestionMapper;
import com.sgc.mapper.QuestionMapper;
import com.sgc.mapper.StudentMapper;
import com.sgc.service.ExamQuestionService;
import com.sgc.service.StudentService;
import com.sgc.utils.PageResult;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Autowired
    private StudentService studentService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ExamMapper examMapper;

    @Override
    public List<ExamQuestion> findAll() {

        List<ExamQuestion> list = examQuestionMapper.findAll();

        return list;
    }

    @Override
    public List<ExamQuestion> findByCondition(ExamQuestion examQuestion) {
        List<ExamQuestion> list = examQuestionMapper.findByCondition(examQuestion);
        return list;
    }

    /**
     * 根据班级名称 向 班级中的所有学生发送试题
     *
     * @param map
     * @return
     */
    @Override
    public boolean sendQuestionToStudentByClassName(Map<String, Object> map) {

        // 1.得到exam实体
        Map exam = (Map) map.get("exam");

        // 2.得到question实体
        List<List<String>> questions = (List<List<String>>) map.get("questions");

        // 3.获得questionId
        List<Integer> questionIds = new ArrayList<>();
        for (List<String> question : questions) {
            String numberStr = question.get(1);
            questionIds.add(Integer.parseInt(numberStr));
        }
        if (questionIds == null || questionIds.size() == 0)
            return false;

        // 4.得到班级名称
        String classNames = (String) exam.get("facedClass");
        String[] split = classNames.split(",");
        List<String> classNameList = new ArrayList<>();
        for (String s : split) {
            classNameList.add(s);
        }

        // 5.得到班级对应的所有学生
        List<Student> students = studentService.findAllStudentsByClassName(classNameList);
//        System.out.println(students);

        // 5.5 把之前存在的所有试题删掉 对应修改试题操作 根据考试id 学生id 删除所有试题
        int examId = (int) exam.get("id");
        List<String> studentIds = new ArrayList<>();
        for (Student student : students) {
            studentIds.add(student.getStudentId());
        }
        int i = -1;
        if(!(studentIds == null || studentIds.size() == 0))
            i = examQuestionMapper.deleteOriginData(examId, studentIds);

        // 6.向所有考试设置学生
        // 7.向所有学生设置试题  exam_id, student_id, question_id, content, score

        for (Student student : students) {
            String studentId = student.getStudentId();
            for (Integer questionId : questionIds) {
                examQuestionMapper.setExamAndStudentAndQuestions(examId, studentId, questionId);
            }
        }
        return true;

    }

    @Override
    public List<List<String>> toGetQuestions(Integer examId) {

        // 拿到试题id
        List<Integer> questionIds = examQuestionMapper.toGetQuestionIdsByExamId(examId);
//        System.out.println(questionIds);

//        List<String> fields = questionMapper.findFieldsById(questionIds);
//        System.out.println(fields);
        // 封装领域和试题id 返回
        List<List<String>> questions = new ArrayList<>();
        for (Integer questionId : questionIds) {
            List<String> question = new ArrayList<>();
            // 根据试题id查找所属领域
            question.add(questionMapper.findFieldsById(questionId));
            question.add(questionId.toString());
            questions.add(question);
        }

        return questions;

    }

    @Override
    public Integer getCountOfQuestionsByExamId(Integer id) {
        Integer count = examQuestionMapper.getCountOfQuestionsByExamId(id);
        return count;
    }

    @Override
    public boolean sendExam(Integer examId) {
        int i = examMapper.sendExam(examId);
//        System.out.println(i + " " + examId);
        if(i == 0) return false;
        return true;
    }


}











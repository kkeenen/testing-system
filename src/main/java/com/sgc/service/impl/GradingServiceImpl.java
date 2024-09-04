package com.sgc.service.impl;

import com.mysql.cj.xdevapi.DatabaseObject;
import com.sgc.mapper.ExamQuestionMapper;
import com.sgc.service.CustomerAppService;
import com.sgc.service.ExamQuestionService;
import com.sgc.vo.AnsweredQuestionVo;
import com.sgc.entity.Student;
import com.sgc.mapper.CustomerAppMapper;
import com.sgc.mapper.GradingMapper;
import com.sgc.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradingServiceImpl implements GradingService {

    @Autowired
    private GradingMapper gradingMapper;
    @Autowired
    private CustomerAppMapper customerAppMapper;

    @Override
    public List<Student> getStudentsByExamId(Integer examId) {
        // 得到所有学生的id
        List<String> studentIds = gradingMapper.getStudentIdsByExamId(examId);

        // 根据ids获得学生实体
        List<Student> list = gradingMapper.getStudentsByIds(studentIds);

        return list;
    }

    @Override
    public Boolean getStatus(String examId, String studentId) {

        String submissionTime = customerAppMapper.getSubmissionTime(studentId, examId);
        boolean status = false;
        if(StringUtils.hasText(submissionTime)){
            status = true;
        }
        return status;
    }


    @Autowired
    private CustomerAppService customerAppService;
    @Override
    public List<AnsweredQuestionVo> getStudentQuestions(String examId, String studentId) {
        /**
         *     private String studentId;
         *     private String examId;
         *     private String questionId;
         *     private String content;
         *     private String answer;
         */

        // 1 根据examid studentid找到所有问题id 并创建vo
        List<String> questionIds = gradingMapper.getQuestionIdsByExamIdAndStudentId(examId,studentId);
//        System.out.println("examId:"+examId+"studentId:"+studentId+"questionIds:"+questionIds);

        List<AnsweredQuestionVo> list = questionIds.stream().map(item -> {
            AnsweredQuestionVo answeredQuestionVo = new AnsweredQuestionVo();
            answeredQuestionVo.setStudentId(studentId);
            answeredQuestionVo.setExamId(examId);
            answeredQuestionVo.setQuestionId(item);

            // 2 根据questionid 获得 问题内容

            answeredQuestionVo.setContent(gradingMapper.getQuestionById(item));

            // 3 根据 examid，studentid，questionId 找到学生作答内容

            answeredQuestionVo.setAnswer(customerAppMapper.
                    getNowQuestionAnswerByExamIdAndStudentIdAndQuestionId(examId,studentId,item));

            // 4 分数为0 分数查数据库获取
            answeredQuestionVo.setScore(gradingMapper.
                    getScoreByExamIdAndStudentIdAndQuestionId(examId,studentId,item));

            // 5 新增去查找图片img  TODO 查询优化
            String key = customerAppMapper.getNowQuestionImgByExamIdAndStudentIdAndQuestionId(examId, studentId, item);

            ResponseEntity<InputStreamResource> responseEntity = customerAppService.downloadImg(key);
            byte[] imageBytes = null;
            String base64Image = null;
            try {
                if(responseEntity.getBody() != null){
                    InputStream inputStream = responseEntity.getBody().getInputStream();
                    imageBytes = inputStream.readAllBytes();
                    base64Image = Base64.getEncoder().encodeToString(imageBytes);
                }
            } catch (IOException e) {

            }
            answeredQuestionVo.setImg(base64Image);


            return answeredQuestionVo;
        }).collect(Collectors.toList());

        return list;


    }

    @Override
    public boolean submitGrading(List<AnsweredQuestionVo> answeredQuestionVo) {

        // 根据 examid studentid  questionid 设置score
        List<Boolean> collect = answeredQuestionVo.stream().map(item -> {
            String studentId = item.getStudentId();
            String examId = item.getExamId();
            String questionId = item.getQuestionId();
            Double score = item.getScore();

            gradingMapper.submitGrading(examId, studentId, questionId, score);
            return true;
        }).collect(Collectors.toList());
        // 先计算总分数 total_score
        List<Double> scores = gradingMapper.getScoresByExamIdAndStudentId(answeredQuestionVo.get(0).getExamId(),
                answeredQuestionVo.get(0).getStudentId()).stream().map(item -> {
            Double score = Double.parseDouble(item);
            return score;
        }).collect(Collectors.toList());
        Double totalScore = 0.0;
        for(Double score : scores){
            totalScore += score;
        }

        // 再去t_exam_grading表 修改批改时间 和 总分数
        gradingMapper.updateGradingTimeAndTotalScoreByStudentIdAndExamId(answeredQuestionVo.get(0).getStudentId(),
                answeredQuestionVo.get(0).getExamId(), totalScore);

        return true;
    }

    @Override
    public Boolean getIsGraded(String examId, String studentId) {

        List<String> list = gradingMapper.getScoresByExamIdAndStudentId(examId, studentId);
        return StringUtils.hasText(list.get(0));

    }
}

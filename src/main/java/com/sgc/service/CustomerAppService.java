package com.sgc.service;

import com.sgc.entity.Exam;
import com.sgc.entity.Question;
import com.sgc.entity.Student;
import com.sgc.vo.CosUploadVo;
import com.sgc.vo.CustomerQuestionReturnVo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CustomerAppService {

    List<Exam> findAll(String studentId);

    String convertStatus(String studentId, String examId, Exam exam);

    Student findStudentById(String studentId);

    List<Question> getQuestionsByStudentIdAndExamId(String studentId, String examId);

    Exam getExamInfoById(String examId);

    CustomerQuestionReturnVo getNowQuestionInfoById(String examId, String studentId, String questionId);

    boolean saveNowQuestion(CustomerQuestionReturnVo question);

    boolean submitExam(String examId, String studentId);

    boolean findAbleToSubmit(String examId, String studentId);

    CosUploadVo upload(MultipartFile file);

    ResponseEntity<InputStreamResource> downloadImg(String key);
}

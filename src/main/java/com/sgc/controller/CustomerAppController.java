package com.sgc.controller;

import com.sgc.entity.Exam;
import com.sgc.entity.Question;
import com.sgc.entity.Student;
import com.sgc.mapper.CustomerAppMapper;
import com.sgc.service.CustomerAppService;
import com.sgc.vo.CosUploadVo;
import com.sgc.vo.CustomerQuestionReturnVo;
import jakarta.websocket.OnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/customer/")
public class CustomerAppController {
    @Autowired
    private CustomerAppService customerAppService;
    @Autowired
    private CustomerAppMapper customerAppMapper;

    @GetMapping("list")
    private ResponseEntity<Map<String, Object>> list(String studentId) {
        List<Exam> list = customerAppService.findAll(studentId);

        list = list.stream().map(item -> {
            item.setStatus(customerAppService.convertStatus(studentId, item.getId().toString(), item));
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("success",true,"records", list));
    }

    @GetMapping("findStudentById")
    private ResponseEntity<Map<String, Object>> findStudentNameById(String studentId) {
        Student student = customerAppService.findStudentById(studentId);
        return ResponseEntity.ok(Map.of("success",true,"data", student));
    }

    @GetMapping("getQuestionsByStudentIdAndExamId")
    private ResponseEntity<Map<String, Object>> getQuestionsByStudentIdAndExamId(@RequestParam("studentId") String studentId,
                                                                                 @RequestParam("examId")String examId) {

        List<Question> questions = customerAppService.getQuestionsByStudentIdAndExamId(studentId, examId);

        return ResponseEntity.ok(Map.of("success",true,"data", questions));
    }

    @GetMapping("getExamInfoById")
    private ResponseEntity<Map<String, Object>> getExamInfoById( String examId) {
        Exam exam = customerAppService.getExamInfoById(examId);
        return ResponseEntity.ok(Map.of("success",true,"data", exam));
    }

    @GetMapping("getNowQuestionInfoById")
    private ResponseEntity<Map<String, Object>> getNowQuestionInfoById(String questionId,
                                                                       String examId,
                                                                       String studentId) {
        CustomerQuestionReturnVo question = customerAppService.getNowQuestionInfoById(examId,studentId,questionId);
        return ResponseEntity.ok(Map.of("success",true,"data", question));
    }

    @PostMapping("saveNowQuestion")
    private ResponseEntity<Map<String, Object>> saveNowQuestion(@RequestBody CustomerQuestionReturnVo question) {
        boolean flg = customerAppService.saveNowQuestion(question);
        return ResponseEntity.ok(Map.of("success",true));
    }

    // 交卷
    @GetMapping("submitExam")
    private ResponseEntity<Map<String, Object>> submitExam(String examId, String studentId) {
        boolean flg = customerAppService.submitExam(examId, studentId);
        return ResponseEntity.ok(Map.of("success",flg));
    }
    // 看能否交卷
    @GetMapping("findAbleToSubmit")
    private ResponseEntity<Map<String, Object>> findAbleToSubmit(String examId, String studentId) {
        boolean flg = customerAppService.findAbleToSubmit(examId, studentId);
        return ResponseEntity.ok(Map.of("success",true, "data", flg));
    }

    @PostMapping("examQuestion/upload")
    public ResponseEntity<Map<String, Object>> upload( MultipartFile file) {
        CosUploadVo uploadVo = customerAppService.upload(file);
        return ResponseEntity.ok(Map.of("success", true, "data", uploadVo));
    }

    @GetMapping("examQuestion/downloadImg")
    private ResponseEntity<Map<String, Object>> downloadImg(String key) throws IOException {
        ResponseEntity<InputStreamResource> responseEntity = customerAppService.downloadImg(key);


        String base64Image = null;
        if(responseEntity.getBody() != null){
            InputStream inputStream = responseEntity.getBody().getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        }

        return ResponseEntity.ok(Map.of("success", true, "data", base64Image));
    }

}

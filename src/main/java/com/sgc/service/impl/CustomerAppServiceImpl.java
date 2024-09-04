package com.sgc.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.sgc.entity.*;
import com.sgc.mapper.CustomerAppMapper;
import com.sgc.service.CustomerAppService;
import com.sgc.vo.CosUploadVo;
import com.sgc.vo.CustomerQuestionReturnVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerAppServiceImpl implements CustomerAppService {

    @Autowired
    private CustomerAppMapper customerAppMapper;

    @Override
    public List<Exam> findAll(String studentId) {

        List<Exam> list = customerAppMapper.findAll(studentId);
        return list;
    }

    /**
     * 状态映射：
     *  根据studentid， 和examId 查找 如果t_exam_grading 有 submission time 状态为已提交
     *  否则为未提交
     * @param studentId
     * @param examId
     * @return
     */
    @Override
    public String convertStatus(String studentId, String examId, Exam exam) {
        String submissionTime = customerAppMapper.getSubmissionTime(studentId, examId);

        String status = null;
        LocalDateTime endTime = exam.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.hasText(submissionTime)) {
            status = "已提交";

        }else if(now.isAfter(endTime)){
            status = "已截止";
        }else {
            status = "正在进行中";
        }

        return status;
    }

    @Override
    public Student findStudentById(String studentId) {
        Student student = customerAppMapper.findStudentById(studentId);
        return student;
    }

    @Override
    public List<Question> getQuestionsByStudentIdAndExamId(String studentId, String examId) {
        // 先查到id
        List<Integer> questionIds = customerAppMapper.getQuestionIdsByStudentIdAndExamId(studentId, examId);
        // 再查问题
        List<Question> questions = customerAppMapper.getQuestionsByIds(questionIds);
        // 设置试题状态
        questions = questions.stream().map(item -> {
            String content = customerAppMapper.getNowQuestionAnswerByExamIdAndStudentIdAndQuestionId(examId, studentId, item.getId().toString());
            String img     = customerAppMapper.getNowQuestionImgByExamIdAndStudentIdAndQuestionId(examId, studentId, item.getId().toString());
            item.setHasText(StringUtils.hasText(content) || StringUtils.hasText(img) ? true : false);
            return item;
        }).collect(Collectors.toList());
        return questions;
    }

    @Override
    public Exam getExamInfoById(String examId) {
        Exam e = customerAppMapper.getExamInfoById(examId);
        return e;
    }

    @Override
    public CustomerQuestionReturnVo getNowQuestionInfoById(String examId, String studentId, String questionId) {
        /**
         *     private String id;
         *     private String content;
         *     private String answer;
         *     private String img ?
         */
        // 得到 nowQuestion
        ExamQuestion examQuestion = customerAppMapper.getNowQuestionInfoById(examId, studentId, questionId);

        // 根据id 得到问题内容
        String questionContent = customerAppMapper.getQuestionContent(examQuestion.getQuestionId());

        // 封装 Vo
        CustomerQuestionReturnVo customerQuestionReturnVo = new CustomerQuestionReturnVo();
        customerQuestionReturnVo.setAnswer(examQuestion.getContent());
        customerQuestionReturnVo.setQuestionId(questionId);
        customerQuestionReturnVo.setContent(questionContent);
        customerQuestionReturnVo.setImg(examQuestion.getImg());
        return customerQuestionReturnVo;
    }

    @Override
    public boolean saveNowQuestion(CustomerQuestionReturnVo question) {
        boolean flg = customerAppMapper.saveNowQuestion(question);
        return flg;
    }

    @Override
    public boolean submitExam(String examId, String studentId) {
        // 交卷功能
        // 先去考生交卷表添加记录
        boolean flg = customerAppMapper.submitExam(examId, studentId);
        return flg;
        // TODO？
    }

    @Override
    public boolean findAbleToSubmit(String examId, String studentId) {

        List<ExamGrading> examGradingList = customerAppMapper.findAbleToSubmit(examId, studentId);

//        System.out.println(examGrading);
        if(examGradingList.size()>1){
            return false;
        }
        if(examGradingList.size()==0){
            return true;
        }
        ExamGrading examGrading = examGradingList.get(0);
        if(examGrading==null || StringUtils.isEmpty(examGrading.getSubmissionTime())){
            return true;
        }
        return false;
    }

    @Override
    public CosUploadVo upload(MultipartFile file) {

        COSClient cosClient = this.getCosClient();

        //元数据信息
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        meta.setContentEncoding("UTF-8");
        meta.setContentType(file.getContentType());

        //向存储桶中保存文件
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //文件后缀名
        String uploadPath = "/testing/" + "answer/" + formattedDate + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;
        // 01.jpg
        // /driver/auth/0o98754.jpg
        PutObjectRequest putObjectRequest = null;
        try {
            //1 bucket名称
            putObjectRequest = new PutObjectRequest("testing-system-1315117695",
                    uploadPath,
                    file.getInputStream(),
                    meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest); //上传文件
        cosClient.shutdown();

        // 获得回显地址
        String imageUrl = this.getImageUrl(uploadPath);

        CosUploadVo uploadVo = new CosUploadVo();
        uploadVo.setUrl(uploadPath);
//        uploadVo.setShowUrl("https://testing-system-1315117695.cos.ap-beijing.myqcloud.com/" + imageUrl);
        uploadVo.setShowUrl(imageUrl);
//        System.out.println(uploadPath);
//        System.out.println(imageUrl);

        return uploadVo;
    }


    public byte[] getImageBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }
    @Override
    public ResponseEntity<InputStreamResource> downloadImg(String key) {
        try {
            System.out.println(key);
            String bucketName = "testing-system-1315117695";
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            COSClient cosClient = this.getCosClient();
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            InputStream inputStream = cosObject.getObjectContent();
            byte[] imageBytes = getImageBytes(inputStream);

            // Set headers for image response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(new ByteArrayInputStream(imageBytes)));
        } catch (Exception e) {

            return ResponseEntity.status(200).build(); // 返回500状态码
        }
    }
    //获取临时签名URL

    public String getImageUrl(String path) {

        if(!StringUtils.hasText(path)) return "";
        //获取cosclient对象
        COSClient cosClient = this.getCosClient();
        //GeneratePresignedUrlRequest
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest("testing-system-1315117695" ,
                        path, HttpMethodName.GET);
        //设置临时URL有效期为15分钟
        Date date = new DateTime().plusMinutes(15).toDate();
        request.setExpiration(date);
        //调用方法获取
        URL url = cosClient.generatePresignedUrl(request);
        cosClient.shutdown();
        return url.toString();

    }

    public COSClient getCosClient() {

        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = "AKIDTxRCrU0TxMi0EIcwDbzNkw8BDAymVFqE";//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        String secretKey = "JbigVArSPEsR0b5UktsdEj7MVm3Oiv12";//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        return cosClient;
    }

}

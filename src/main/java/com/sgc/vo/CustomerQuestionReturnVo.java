package com.sgc.vo;

import lombok.Data;

@Data
public class CustomerQuestionReturnVo {

    private String studentId;
    private String examId;
    private String questionId;
    private String content;
    private String answer;
    private String img;
}

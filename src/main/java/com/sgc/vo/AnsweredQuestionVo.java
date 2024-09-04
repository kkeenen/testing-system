package com.sgc.vo;

import lombok.Data;

@Data
public class AnsweredQuestionVo {
    private String studentId;
    private String examId;
    private String questionId;
    private String content;
    private String answer;
    private Double score = null;
    private String img;
}

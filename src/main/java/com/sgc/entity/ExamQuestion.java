package com.sgc.entity;

import lombok.Data;

@Data
public class ExamQuestion {
    private Integer examId;
    private String questionId;
    private String studentId;
    private Double score;
    private String content;
    private String img;
}

package com.sgc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamGrading {
    private String id;
    private String examId;
    private String studentId;
    private LocalDateTime gradingTime;
    private LocalDateTime submissionTime;
    private String totalScore;

}

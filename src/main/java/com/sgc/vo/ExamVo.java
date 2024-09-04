package com.sgc.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamVo {
    private Integer id;
    private String title;
    private String facedClass;
    private String status; // 1已下发 2已提交 3已批改
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer countOfQuestions = 123;

    // for count
    private Integer studentCount;
    private Integer submitCount;
    private Integer gradeCount;
}

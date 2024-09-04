package com.sgc.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Exam {
    private Integer id;
    private String title;
    private String facedClass;
    private String status; // 1已下发 2已提交 3已批改
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer countOfQuestions = 123;
}

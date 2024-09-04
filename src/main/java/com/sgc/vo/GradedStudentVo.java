package com.sgc.vo;

import lombok.Data;

@Data
public class GradedStudentVo {
    private Integer id;
    private String studentId;
    private String name;
    private String className;

    private Boolean status;// 意味是否交卷 true为交 false为未交
    private Boolean isGraded;
}

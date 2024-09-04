package com.sgc.entity;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String content;
    private String ReferenceAnswer;
    private String field;

    // 用于客户端状态显示
    private Boolean hasText=false;
}

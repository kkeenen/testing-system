package com.sgc.vo;

import lombok.Data;

import java.util.List;

@Data
public class EditQuestionVo {
    private String value;
    private String label;
    private List<EditQuestionVo> children;
}

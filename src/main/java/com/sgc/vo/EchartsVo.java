package com.sgc.vo;

import lombok.Data;

@Data
public class EchartsVo {
    private String name;
    private Integer value;

    public EchartsVo() {}
    public EchartsVo(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}

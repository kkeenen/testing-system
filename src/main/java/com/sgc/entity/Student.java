package com.sgc.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Student implements Serializable {
    private Integer id;
    private String studentId;
    private String name;
    private String className;
    private String gender;
    private String phone;
    private String qq;
    private String major;
    private LocalDate entryDate;
}

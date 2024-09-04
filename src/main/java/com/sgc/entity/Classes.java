package com.sgc.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Classes {
    private Integer id;
    private String classId;
    private String className;
    private Integer numberOfStudents;
    private String startDate ;
    private String endDate ;
}

package com.sgc.mapper;

import com.sgc.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> findAll(@Param("pageSize")Integer pageSize, @Param("offset")Integer offset);

    int findCount();

    List<String> findClasses();

    List<Student> findByCondition(@Param("pageSize")Integer pageSize,
                                  @Param("offset")int offset,
                                  @Param("student")Student student);


    int add(Student student);

    int edit(Student student);

    int deleteByIds(@Param("ids")Integer[] ids);

    List<Student> findAllStudentsByClassName(List<String> classNameList);


    int resetStudentClassByClassName(String className);

    int getMaleCount();

    int getFemaleCount();

    List<String> getAllMajor();

    Integer getMajorCount(String major);


    void addToUser(String studentId);
}

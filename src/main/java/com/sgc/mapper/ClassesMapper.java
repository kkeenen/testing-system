package com.sgc.mapper;

import com.sgc.entity.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassesMapper {

    List<Classes> findAll(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    int findAllCount();

    List<Classes> findByCondition(@Param("pageSize") Integer pageSize,
                                  @Param("offset") int offset,
                                  @Param("class") Classes classes);

    Integer findConditionCount(@Param("pageSize") Integer pageSize,
                           @Param("offset") int offset,
                           @Param("class") Classes classes);


    int add(Classes classes);

    int edit(Classes classes);

    int deleteByIds(@Param("ids") Integer[] ids);

    int getStudentNumber(String className);

    String getClassNameByClassId(String classId);
}

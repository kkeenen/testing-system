package com.sgc.service.impl;

import com.sgc.entity.Classes;
import com.sgc.entity.Student;
import com.sgc.mapper.StudentMapper;
import com.sgc.utils.PageResult;
import com.sgc.mapper.ClassesMapper;
import com.sgc.service.ClassesService;
import com.sgc.utils.MyDateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesMapper classesMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Classes> findAll(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Classes> classes = classesMapper.findAll(pageSize, offset);
        int count =  classesMapper.findAllCount();
        PageResult<Classes> page = new PageResult<>();
        page.setTotal(count);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setData(classes);
        return page;
    }

    @Override
    public PageResult<Classes> findByCondition(Integer pageNo, Integer pageSize, Classes classes) {
//        if(!StringUtils.isEmpty(classes.getStartDate())){
//            classes.setStartDate(classes.getStartDate().substring(0,10));
//        }
//        if(!StringUtils.isEmpty(classes.getEndDate())){
//            classes.setEndDate(classes.getEndDate().substring(0,10));
//        }

        int offset = (pageNo - 1) * pageSize;
        List<Classes> list = classesMapper.findByCondition(pageSize, offset, classes);
        Integer count =  classesMapper.findConditionCount(pageSize, offset, classes);
//        int count = list.size();
        PageResult<Classes> page = new PageResult<>();
        page.setTotal(count==null?0:count);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setData(list);
        return page;
    }

    @Override
    public int add(Classes classes) {
        classes.setStartDate(MyDateTools.convertDate(classes.getStartDate()));
        classes.setEndDate(MyDateTools.convertDate(classes.getEndDate()));
        int i = classesMapper.add(classes);
        return i;
    }

    @Override
    public int edit(Classes classes) {
        classes.setStartDate(MyDateTools.convertDate(classes.getStartDate()));
        classes.setEndDate(MyDateTools.convertDate(classes.getEndDate()));
        int i = classesMapper.edit(classes);
        return i;
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        // 删除班级操作：
        // 1根据班级id 找到班级name 根据班级name找到在本班级的所有学生 把学生的班级字段置空

        for (Integer id : ids) {
            String className = classesMapper.getClassNameByClassId(id.toString());
            int i = studentMapper.resetStudentClassByClassName(className);
        }

        // 2删除班级表中班级记录
        int i = classesMapper.deleteByIds(ids);
        return i;
    }
}

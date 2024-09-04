package com.sgc.service;

import com.sgc.entity.Classes;
import com.sgc.utils.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface ClassesService {

    PageResult<Classes> findAll(Integer pageNo, Integer pageSize);

    PageResult<Classes> findByCondition(Integer pageNo, Integer pageSize, Classes classes);

    int add(Classes classes);

    int edit(Classes classes);

    int deleteByIds(Integer[] ids);
}

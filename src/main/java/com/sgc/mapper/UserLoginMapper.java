package com.sgc.mapper;

import com.sgc.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginMapper {


    User findAdminByUsername(String username);

    User findUserByUsername(String username);
}

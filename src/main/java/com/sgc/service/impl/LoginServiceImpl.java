package com.sgc.service.impl;

import com.sgc.entity.User;
import com.sgc.mapper.UserLoginMapper;
import com.sgc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public User findAdminByUsername(String username) {
        return userLoginMapper.findAdminByUsername(username);
    }

    @Override
    public User findUserByUsername(String username) {
        return userLoginMapper.findUserByUsername(username);
    }
}

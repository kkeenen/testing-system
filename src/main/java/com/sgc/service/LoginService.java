package com.sgc.service;

import com.sgc.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    User findAdminByUsername(String username);

    User findUserByUsername(String username);
}

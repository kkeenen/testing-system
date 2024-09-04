package com.sgc.controller;


import com.sgc.entity.User;
import com.sgc.service.LoginService;
import com.sgc.utils.JwtUtils;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/", produces = "application/json;charset=utf-8")
public class LoginController {

    private static final PasswordEncryptor encryptor = new StrongPasswordEncryptor();
    private LoginService loginService;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Autowired
    public void setUserService(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("admins/login")
    public Map<String, Object> adminsLogin(@RequestBody User user, HttpSession session) {
        if (!StringUtils.hasText(user.getCaptcha())) {
            return Map.of("success", false, "error", "验证码不可为空");
        }

        String answer = (String) session.getAttribute("captcha");
        if (!answer.equalsIgnoreCase(user.getCaptcha())) {
            return Map.of("success", false, "error", "验证码不正确");
        }

        if (!StringUtils.hasText(user.getUsername())) {
            return Map.of("success", false, "error", "用户名不可为空");
        }

        if (!StringUtils.hasText(user.getPassword())) {
            return Map.of("success", false, "error", "密码不可为空");
        }

        //1。登录验证
//        User user = this.userService.findByUsername(account.getUsername());
        String username = user.getUsername();

        User standard_user = loginService.findAdminByUsername(username);

        if (standard_user == null) {
            return Map.of("success", false, "error", "无此用户");
        }


        //密码校验
        if (!encryptor.checkPassword(user.getPassword(), standard_user.getPassword())) {
            return Map.of("success", false, "error", "密码不正确");
        }

        //2。颁发令牌
        Map<String, Object> data = Map.of("username", user.getUsername());
        String jwt = JwtUtils.encode(username, data, jwtSecretKey);

        return Map.of("success", true, "jwt", jwt);
    }

    @GetMapping("admins/captcha")
    public ResponseEntity<Map<String,Object>> adminsCaptcha(HttpServletResponse resp, HttpSession session) throws IOException {

        //生成验证码
        SpecCaptcha cap = new SpecCaptcha(170, 35, 4);

        //设置响应头
        resp.setContentType("image/gif");
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0L);


        //将答案存储到session中
        session.setAttribute("captcha", cap.text().toLowerCase());
        cap.out(resp.getOutputStream());
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/customer/users/login")
    public Map<String, Object> usersLogin(@RequestBody User user, HttpSession session) {

        if (!StringUtils.hasText(user.getCaptcha())) {
            return Map.of("success", false, "error", "验证码不可为空");
        }

        String answer = (String) session.getAttribute("captcha");
        if (!answer.equalsIgnoreCase(user.getCaptcha())) {
            return Map.of("success", false, "error", "验证码不正确");
        }

        if (!StringUtils.hasText(user.getUsername())) {
            return Map.of("success", false, "error", "用户名不可为空");
        }

        if (!StringUtils.hasText(user.getPassword())) {
            return Map.of("success", false, "error", "密码不可为空");
        }

        //1。登录验证
//        User user = this.userService.findByUsername(account.getUsername());
        String username = user.getUsername();

        User standard_user = loginService.findUserByUsername(username);

        if (standard_user == null) {
            return Map.of("success", false, "error", "无此用户");
        }


        //密码校验
        if (!encryptor.checkPassword(user.getPassword(), standard_user.getPassword())) {
            return Map.of("success", false, "error", "密码不正确");
        }

        //2。颁发令牌
        Map<String, Object> data = Map.of("username", user.getUsername());
        String jwt = JwtUtils.encode(username, data, jwtSecretKey);

        return Map.of("success", true, "jwt", jwt);
    }

    @GetMapping("/users/captcha")
    public ResponseEntity<Map<String,Object>> usersCaptcha(HttpServletResponse resp, HttpSession session) throws IOException {

        //生成验证码
        SpecCaptcha cap = new SpecCaptcha(170, 35, 4);

        //设置响应头
        resp.setContentType("image/gif");
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0L);


        //将答案存储到session中
        session.setAttribute("captcha", cap.text().toLowerCase());
        cap.out(resp.getOutputStream());
        return ResponseEntity.ok(Map.of("success", true));
    }


}

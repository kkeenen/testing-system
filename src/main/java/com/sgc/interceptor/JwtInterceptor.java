package com.sgc.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;

import com.sgc.utils.JwtUtils;
import com.sgc.utils.WebAppUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    private final String jwtSecretKey;

    public JwtInterceptor(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse resp, Object handler) throws Exception {
        //1.从客户端请求中获取jwt。
        String jwt = req.getHeader("jwt");//请求头名称任意，Authorization

        //2.解密
        try {
            DecodedJWT dj = JwtUtils.decode(jwt, jwtSecretKey);

            return true;

        } catch (Exception e) {
            //throw new RuntimeException(e);
            WebAppUtils.writeJson(resp, Map.of("code", 401, "error", "jwt无效"));
            return false;//不能放行
        }
    }
}

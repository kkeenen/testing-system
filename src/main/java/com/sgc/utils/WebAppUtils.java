package com.sgc.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class WebAppUtils {


    /**
     * 响应一个json数据
     */
    public static void writeJson(HttpServletResponse resp, String json) {
        resp.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter out = resp.getWriter();
            out.write(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJson(HttpServletResponse resp, Map<String, Object> map) {
        writeJson(resp, JSON.toJSONString(map));
    }
}

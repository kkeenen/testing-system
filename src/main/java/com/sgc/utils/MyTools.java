package com.sgc.utils;

import com.sgc.mapper.ClassesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.StringJoiner;


public class MyTools {

    // 默认日期时间格式
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    // 默认格式化器
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);

    /**
     * 将字符串转换为 LocalDateTime
     *
     * @param dateTimeStr 要转换的日期时间字符串
     * @return 转换后的 LocalDateTime 对象，如果转换失败则返回 null
     */

    public static LocalDateTime convertToLocalDateTime(String dateTimeStr) {
        return convertToLocalDateTime(dateTimeStr, DEFAULT_FORMATTER);
    }

    /**
     * 将字符串转换为 LocalDateTime，使用指定的格式化器
     *
     * @param dateTimeStr 要转换的日期时间字符串
     * @param formatter 日期时间格式化器
     * @return 转换后的 LocalDateTime 对象，如果转换失败则返回 null
     */

    public static LocalDateTime convertToLocalDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            // 记录异常信息
            System.err.println("日期时间字符串解析失败: " + e.getMessage());
            return null;
        }
    }



}

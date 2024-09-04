package com.sgc.utils;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class MyDateTools {

    public static String convertDateFormat(String inputDate) {
        // 定义输入和输出日期格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析输入日期字符串
        ZonedDateTime dateTime = ZonedDateTime.parse(inputDate, inputFormatter);

        // 将日期时间转换为所需格式的字符串
        return dateTime.toLocalDate().plusDays(1).format(outputFormatter);
    }

    public static String convertDate(String inputDate) {
        if(inputDate.length() <= 11) return inputDate;
        return convertDateFormat(inputDate);
    }


}

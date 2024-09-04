package com.sgc.test;

import com.sgc.entity.Student;

import java.lang.reflect.Constructor;

public class Reflect {
    public static void main(String[] args) {
        try {

            Class<?> clazz = Class.forName("com.sgc.entity.Student");
            Constructor<?> constructor = clazz.getConstructor();
            Student student = (Student) constructor.newInstance();
            System.out.println(student);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

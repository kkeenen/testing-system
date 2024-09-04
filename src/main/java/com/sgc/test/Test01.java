package com.sgc.test;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class Test01 {
    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

    }
}

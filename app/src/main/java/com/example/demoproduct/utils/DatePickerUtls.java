package com.example.demoproduct.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class DatePickerUtls {
    public static String dayOrMonthWith2Digits(int day) {

        if(String.valueOf(day).length() == 1)
            return "0" +day;

        return String.valueOf(day);
    }


    public static String convert(int year, int month, int day) {
        return getDateWithMonthMMM(year +"-" +dayOrMonthWith2Digits(month +1) +"-" +  dayOrMonthWith2Digits(day));
//		return dayOrMonthWith2Digits(day) +"-" +dayOrMonthWith2Digits(month) +"-" + year;
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDateWithMonthMMM(String dateWithMonthMM) {

        String dateWithMonthMMM = null;
        SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateInDate = sdfOld.parse(dateWithMonthMM);
            dateWithMonthMMM = sdfNew.format(dateInDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateWithMonthMMM;
    }


}



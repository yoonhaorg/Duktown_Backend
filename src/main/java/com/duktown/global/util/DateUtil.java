package com.duktown.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final long SECOND = 1000; // MilliSecond
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;

    public static String convert(LocalDateTime dateTime){
        LocalDateTime now = LocalDateTime.now();
        // 현재 시간
        long curTime = now.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 등록 시간
        long regTime = dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 지난 시간
        long diffTime = Math.abs(curTime - regTime);

        String msg;

        if(diffTime / SECOND < 60) {
            msg = diffTime / SECOND + "초 전";
        } else if (diffTime / MINUTE < 60) {
            msg = diffTime / MINUTE + "분 전";
        } else if (diffTime / HOUR < 24) {
            msg = DateTimeFormatter.ofPattern("HH:mm").format(dateTime);
        } else if (dateTime.getYear() == now.getYear()) {
            msg = DateTimeFormatter.ofPattern("MM/dd HH:mm").format(dateTime);
        } else {
            msg = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm").format(dateTime);
        }

        return msg;
    }

    public static String convertToAMPMFormat(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        // 현재 시간
        long curTime = now.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 등록 시간
        long regTime = dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 남은 시간
        long diffTime = Math.abs(curTime - regTime);

        String msg;

        if (diffTime / HOUR < 24 && dateTime.getDayOfYear() == now.getDayOfYear()) {
            msg = DateTimeFormatter.ofPattern("hh:mm a").format(dateTime);
        } else if (dateTime.getYear() == now.getYear()) {
            msg = DateTimeFormatter.ofPattern("MM/dd hh:mm a").format(dateTime);
        } else {
            msg = DateTimeFormatter.ofPattern("yy/MM/dd hh:mm a").format(dateTime);
        }

        return msg;
    }
}

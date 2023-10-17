package com.duktown.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final long SECOND = 1000; // MilliSecond
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    public static String convert(LocalDateTime dateTime){
        LocalDateTime now = LocalDateTime.now();
        // 현재 시간
        long curTime = now.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 등록 시간
        long regTime = dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        // 지난 시간
        long diffTime = curTime - regTime;

        String msg;

        if(diffTime / SECOND < 60) {
            msg = diffTime / SECOND + "초 전";
        } else if (diffTime / MINUTE < 60) {
            msg = diffTime / MINUTE + "분 전";
        } else if (diffTime / HOUR < 24) {
            msg = diffTime / HOUR + "시간 전";
        } else if (diffTime / DAY == 1) {
            msg = "어제";
        } else if (diffTime / DAY < 30) {
            msg = diffTime / DAY + "일 전";
        } else {
            msg = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm").format(dateTime);
        }

        return msg;
    }
}

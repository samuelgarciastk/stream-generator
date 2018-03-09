package io.transwarp.streamgenerator.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class TimeGenerator {
    public static String randomDate(String beginDate, String endDate, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            long begin = sf.parse(beginDate).getTime();
            long end = sf.parse(endDate).getTime();
            if (begin > end) return null;
            return sf.format(new Date(begin + (long) (Math.random() * (end - begin))));
        } catch (ParseException e) {
            System.out.println("Date format error: " + format);
            e.printStackTrace();
        }
        return null;
    }

    public static String randomTime() {
        return randomDate("00:00:00", "24:00:00", "HH:mm:ss");
    }

    public static String transform(String date, String inFormat, String outFormat) {
        SimpleDateFormat inDateFormat = new SimpleDateFormat(inFormat);
        SimpleDateFormat outDateFormat = new SimpleDateFormat(outFormat);
        try {
            Date time = inDateFormat.parse(date);
            return outDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Date cannot be transformed from \"" + inFormat + "\" to \"" + outFormat + "\": " + date);
        throw new NullPointerException();
    }

    public static String randomDateWithTrans(String beginDate, String endDate, String format) {
        return transform(randomDate(beginDate, endDate, "yyyyMMdd"), "yyyyMMdd", format);
    }
}

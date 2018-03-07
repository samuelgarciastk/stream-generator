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
            e.printStackTrace();
        }
        return null;
    }

    public static String randomTime() {
        return randomDate("00:00:00", "24:00:00", "HH:mm:ss");
    }
}

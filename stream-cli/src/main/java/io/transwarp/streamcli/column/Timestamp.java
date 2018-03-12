package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.TimeGenerator;
import io.transwarp.streamcli.common.DataGen;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random timestamp from a given format.
 * Format: {format} & {begin time} & {end time}
 * E.g., yyyy-MM-dd HH:mm:ss & 1970-01-01 11:11:11 & 2017-12-31 22:22:22
 */
public class Timestamp implements DataGen {
    private String format;
    private String begin;
    private String end;

    public Timestamp(String param) {
        String[] timeArr = param.split("&");
        format = timeArr[0].trim();
        begin = timeArr[1].trim();
        end = timeArr[2].trim();
    }

    @Override
    public String nextRecord() {
        return TimeGenerator.randomDate(begin, end, format);
    }
}

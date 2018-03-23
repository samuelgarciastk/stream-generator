package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;

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

    public Timestamp(List<String> configs) {
        format = configs.get(0);
        begin = configs.get(1);
        end = configs.get(2);
    }

    @Override
    public String nextRecord() {
        return TimeGenerator.randomDate(begin, end, format);
    }
}

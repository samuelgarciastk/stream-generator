package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.TimeGenerator;

/**
 * Author: stk
 * Date: 2018/3/8
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

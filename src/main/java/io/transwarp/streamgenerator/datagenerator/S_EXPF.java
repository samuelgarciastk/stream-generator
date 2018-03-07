package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class S_EXPF implements DataGen {
    private AtomicLong base = new AtomicLong();
    private String beginDate;
    private String endDate;
    private String prefix;

    public S_EXPF(Properties props) {
        base.set(Long.parseLong(props.getProperty("safe.base")));
        beginDate = props.getProperty("safe.begin.year") + "-01-01";
        endDate = props.getProperty("safe.end.year") + "-12-31";
        prefix = props.getProperty("safe.prefix");
    }

    @Override
    public String nextRecord() {
        List<String> result = new ArrayList<>();
        result.add(String.format("%018d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDate(beginDate, endDate, "yyyy-MM-dd"));
        return result.stream().collect(Collectors.joining(","));
    }
}

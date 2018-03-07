package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class S_EXPF implements DataGen {
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("safe.base")));
    private String beginDate = Generator.props.getProperty("safe.begin.year") + "-01-01";
    private String endDate = Generator.props.getProperty("safe.end.year") + "-12-31";
    private String prefix = Generator.props.getProperty("safe.prefix");

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        result.add(String.format("%018d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDate(beginDate, endDate, "yyyy-MM-dd"));
        return result.toString();
    }
}

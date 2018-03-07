package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.columngenerator.SafeInfo;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class S_CORP_RCV implements DataGen {
    private AtomicLong base = new AtomicLong();
    private SafeInfo info = new SafeInfo();
    private String beginDate;
    private String endDate;
    private String prefix;
    private String delimiter;

    public S_CORP_RCV(Properties props) {
        base.set(Long.parseLong(props.getProperty("safe.base")));
        beginDate = props.getProperty("safe.begin.year") + "-01-01";
        endDate = props.getProperty("safe.end.year") + "-12-31";
        prefix = props.getProperty("safe.prefix");
        delimiter = props.getProperty("delimiter");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        result.add(String.format("%022d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDate(beginDate, endDate, "yyyy-MM-dd"));
        result.add(info.nextRecord());
        result.add(String.valueOf((int) (Math.random() * 10000)));
        result.add(StringGenerator.randomNumber(6));
        return result.toString();
    }
}

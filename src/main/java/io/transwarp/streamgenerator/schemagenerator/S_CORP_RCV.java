package io.transwarp.streamgenerator.schemagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.columngenerator.SafeInfo;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class S_CORP_RCV implements DataGen {
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("safe.base")));
    private SafeInfo info = new SafeInfo();
    private String beginDate = Generator.props.getProperty("safe.date.begin.year") + "0101";
    private String endDate = Generator.props.getProperty("safe.date.end.year") + "1231";
    private String prefix = Generator.props.getProperty("safe.prefix");

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        result.add(String.format("%022d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDateWithTrans(beginDate, endDate, Generator.props.getProperty("safe.date.format")));
        result.add(info.nextRecord());
        result.add(String.valueOf((int) (Math.random() * 10000)));
        result.add(StringGenerator.randomNumber(6));
        return result.toString();
    }
}

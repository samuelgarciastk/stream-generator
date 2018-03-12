package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: stk
 * Date: 2018/3/5
 * <p>
 * Generate random EXPF data.
 * Format: CUSTOM_RPT_NO, 企业组织机构代码, EXP_DATE
 * E.g., 000009900976892116,013499322,1967-08-02
 */
public class S_EXPF implements DataGen {
    private final String beginDate = Generator.props.getProperty("safe.date.begin.year") + "0101";
    private final String endDate = Generator.props.getProperty("safe.date.end.year") + "1231";
    private final String prefix = Generator.props.getProperty("safe.prefix");
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("safe.base")));

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        result.add(String.format("%018d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDateWithTrans(beginDate, endDate, Generator.props.getProperty("safe.date.format")));
        return result.toString();
    }
}

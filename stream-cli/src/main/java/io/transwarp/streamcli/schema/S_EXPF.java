package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.Properties;
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
    private String delimiter;
    private AtomicLong base;
    private String beginDate;
    private String endDate;
    private String prefix;
    private String dateFormat;

    public S_EXPF(Properties props) {
        delimiter = props.getProperty("delimiter");
        base = new AtomicLong(Long.parseLong(props.getProperty("safe.base")));
        beginDate = props.getProperty("safe.date.begin.year") + "0101";
        endDate = props.getProperty("safe.date.end.year") + "1231";
        prefix = props.getProperty("safe.prefix");
        dateFormat = props.getProperty("safe.date.format");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        result.add(String.format("%018d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDateWithTrans(beginDate, endDate, dateFormat));
        return result.toString();
    }
}

package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.column.SafeInfo;
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
 * Generate random RCV data.
 * Format: RPTNO, 企业组织机构代码, RCV_DATE, SAFECODE, 收汇美元金额, 交易代码
 * E.g., 0000000000101001878430,213499332,1974-09-19,161000,546,880544
 */
public class S_CORP_RCV implements DataGen {
    private SafeInfo info;
    private String delimiter;
    private AtomicLong base;
    private String beginDate;
    private String endDate;
    private String prefix;
    private String dateFormat;

    public S_CORP_RCV(Properties props) {
        info = new SafeInfo(null);
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
        result.add(String.format("%022d", base.addAndGet((int) (Math.random() * 500 + 1))));
        result.add(prefix + StringGenerator.randomNumber(9 - prefix.length()));
        result.add(TimeGenerator.randomDateWithTrans(beginDate, endDate, dateFormat));
        result.add(info.nextRecord());
        result.add(String.valueOf((int) (Math.random() * 10000)));
        result.add(StringGenerator.randomNumber(6));
        return result.toString();
    }
}

package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.column.SafeInfo;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

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
    private final String beginDate = Generator.props.getProperty("safe.date.begin.year") + "0101";
    private final String endDate = Generator.props.getProperty("safe.date.end.year") + "1231";
    private final String prefix = Generator.props.getProperty("safe.prefix");
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("safe.base")));
    private SafeInfo info = new SafeInfo();

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

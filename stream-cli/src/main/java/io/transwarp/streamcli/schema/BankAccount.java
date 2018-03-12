package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: stk
 * Date: 2018/3/5
 * <p>
 * Generate random bank account information.
 * Format: 开户行, 卡号, 开户日期, 开户人身份证, 开户人姓名, 开户名手机号
 * E.g., 中国工商银行,0000020622204934,19920321,230802195001016154,饶佳瑞,159612236886
 * Configuration files: bank
 */
public class BankAccount implements DataGen {
    private static final List<String> bankName = ConfLoader.loadConf("bank");
    private People people = new People();
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("bank.account.base")));

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        result.add(bankName.get((int) (Math.random() * bankName.size())));
        result.add(String.format("%0" + Generator.props.getProperty("bank.account.id.length") + "d", base.addAndGet((int) (Math.random() * 1000 + 1))));
        String[] peopleArr = people.nextRecord().split(Generator.delimiter);
        result.add(TimeGenerator.randomDate(peopleArr[4], Generator.props.getProperty("bank.account.date.end"), "yyyyMMdd"));
        result.add(peopleArr[3]);
        result.add(peopleArr[0]);
        result.add(peopleArr[5]);
        return result.toString();
    }
}

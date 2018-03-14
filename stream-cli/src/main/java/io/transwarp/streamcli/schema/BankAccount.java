package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;
import java.util.Properties;
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
    private List<String> bankName;
    private People people;
    private String delimiter;
    private AtomicLong base;
    private String IDLength;
    private String endDate;

    public BankAccount(Properties props) {
        bankName = ConfLoader.loadConf("bank");
        people = new People(props);
        delimiter = props.getProperty("delimiter");
        base = new AtomicLong(Long.parseLong(props.getProperty("bank.account.base")));
        IDLength = props.getProperty("bank.account.id.length");
        endDate = props.getProperty("bank.account.date.end");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        result.add(bankName.get((int) (Math.random() * bankName.size())));
        result.add(String.format("%0" + IDLength + "d", base.addAndGet((int) (Math.random() * 1000 + 1))));
        String[] peopleArr = people.nextRecord().split(delimiter);
        result.add(TimeGenerator.randomDate(peopleArr[4], endDate, "yyyyMMdd"));
        result.add(peopleArr[3]);
        result.add(peopleArr[0]);
        result.add(peopleArr[5]);
        return result.toString();
    }
}

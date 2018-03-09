package io.transwarp.streamgenerator.schemagenerator;

import io.transwarp.streamgenerator.Consumer;
import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class BankAccount implements DataGen {
    private static final List<String> bankName = ConfLoader.loadConf("bank");
    private static final List<Integer> cardNum = ConfLoader.loadConf("bank_account_conf").stream().map(Integer::parseInt).collect(Collectors.toList());
    private AtomicLong base = new AtomicLong(Long.parseLong(Generator.props.getProperty("bank.account.base")));
    private Consumer consumer;

    public BankAccount() {
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", "1");
        consumerProps.put("group.id", "BankAccount");
        consumer = new Consumer(Generator.props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        String[] people = consumer.getOneValue().split(",");
        int size = cardNum.get((int) (Math.random() * cardNum.size()));
        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        for (int i = 0; i < size; i++) {
            StringJoiner line = new StringJoiner(Generator.delimiter);
            line.add(bankName.get((int) (Math.random() * bankName.size())));
            line.add(String.format("%0" + Generator.props.getProperty("bank.account.id.length") + "d", base.addAndGet((int) (Math.random() * 1000 + 1))));
            line.add(TimeGenerator.randomDate(people[4], Generator.props.getProperty("bank.account.date.end"), "yyyyMMdd"));
            line.add(people[3]);
            line.add(people[0]);
            line.add(people[5]);
            result.add(line.toString());
        }
        return result.toString();
    }
}

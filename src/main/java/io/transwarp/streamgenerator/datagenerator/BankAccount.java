package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.Consumer;
import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BankAccount implements DataGen {
    private static final List<String> bankName = ConfLoader.loadConf("bank");
    private static final List<Integer> cardNum = ConfLoader.loadConf("bank_account_conf").stream().map(Integer::parseInt).collect(Collectors.toList());
    private AtomicLong base = new AtomicLong();
    private Consumer consumer;

    public BankAccount(Properties props) {
        base.set(Long.parseLong(props.getProperty("bank.account.base")));
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", "1");
        consumerProps.put("group.id", "BankAccount");
        consumer = new Consumer(props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        String[] people = consumer.getOneValue().split(",");
        int size = cardNum.get((int) (Math.random() * cardNum.size()));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<String> line = new ArrayList<>();
            line.add(bankName.get((int) (Math.random() * bankName.size())));
            line.add(String.format("%016d", base.addAndGet((int) (Math.random() * 1000 + 1))));
            line.add(TimeGenerator.randomDate(people[4], "20171231", "yyyyMMdd"));
            line.add(people[3]);
            line.add(people[0]);
            line.add(people[5]);
            result.add(line.stream().collect(Collectors.joining(",")));
        }
        return result.stream().collect(Collectors.joining(System.getProperty("line.separator")));
    }
}

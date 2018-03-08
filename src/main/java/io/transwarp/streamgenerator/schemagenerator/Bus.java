package io.transwarp.streamgenerator.schemagenerator;

import io.transwarp.streamgenerator.Consumer;
import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/3/8
 */
public class Bus implements DataGen {
    private static final List<String> timeTable = ConfLoader.loadData("bus");
    private static final List<Integer> weight = ConfLoader.loadConf("bus_travel").stream().map(Integer::parseInt).collect(Collectors.toList());
    private Consumer consumer;

    public Bus() {
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", "1");
        consumerProps.put("group.id", "Bus");
        consumer = new Consumer(Generator.props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        String[] people = consumer.getOneValue().split(",");
        int size = weight.get((int) (Math.random() * weight.size()));
        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        for (int i = 0; i < size; i++) {
            StringJoiner line = new StringJoiner(Generator.delimiter);
            String[] bus = timeTable.get((int) (Math.random() * timeTable.size())).split(",");
            line.add(people[0]);
            line.add(people[1]);
            line.add(people[3]);
            line.add(TimeGenerator.randomDate(people[4], "20171231", "yyyyMMdd"));
            line.add(String.valueOf((int) (Math.random() * 5 + 1)));
            line.add(bus[0]);
            line.add(bus[1]);
            line.add(String.valueOf((int) (Math.random() * 60 + 1)));
            line.add(bus[2]);
            line.add(bus[3]);
            result.add(line.toString());
        }
        return result.toString();
    }
}

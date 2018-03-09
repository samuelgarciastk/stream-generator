package io.transwarp.streamgenerator.schema;

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
 * Date: 2018/3/9
 */
public class Train implements DataGen {
    private static final List<String> timeTable = ConfLoader.loadData("train");
    private static final List<Integer> weight = ConfLoader.loadConf("train_travel").stream().map(Integer::parseInt).collect(Collectors.toList());
    private Consumer consumer;

    public Train() {
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", "1");
        consumerProps.put("group.id", "Train");
        consumer = new Consumer(Generator.props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        String[] people = consumer.getOneValue().split(",");
        int size = weight.get((int) (Math.random() * weight.size()));
        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        for (int i = 0; i < size; i++) {
            StringJoiner line = new StringJoiner(Generator.delimiter);
            String[] train = timeTable.get((int) (Math.random() * timeTable.size())).split(",");
            line.add(people[0]);
            line.add(people[1]);
            line.add(people[3]);
            line.add(train[0]);
            line.add(TimeGenerator.randomDateWithTrans(people[4],
                    Generator.props.getProperty("travel.date.end"),
                    Generator.props.getProperty("travel.date.format")));
            line.add(String.valueOf((int) (Math.random() * 16 + 1)));
            line.add(String.valueOf((int) (Math.random() * 40 + 1)));
            line.add(train[1]);
            line.add(train[2]);
            line.add(train[3]);
            result.add(line.toString());
        }
        return result.toString();
    }
}

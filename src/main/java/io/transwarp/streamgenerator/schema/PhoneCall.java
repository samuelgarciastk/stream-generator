package io.transwarp.streamgenerator.schema;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class PhoneCall implements DataGen {
    private int windowSize = Integer.parseInt(Generator.props.getProperty("phone.window.size"));
    private double ratio = Double.parseDouble(Generator.props.getProperty("phone.ratio"));
    private int durationBase = Integer.parseInt(Generator.props.getProperty("phone.duration.base"));
    private Consumer consumer;

    public PhoneCall() {
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", windowSize);
        consumerProps.put("group.id", "PhoneCall");
        consumer = new Consumer(Generator.props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        List<String> people = consumer.getValues(windowSize);
        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        for (int i = 0; i < windowSize * ratio; i++) {
            StringJoiner line = new StringJoiner(Generator.delimiter);
            int index1 = (int) (Math.random() * windowSize);
            int index2 = (int) (Math.random() * windowSize);
            while (index1 == index2) index2 = (int) (Math.random() * windowSize);
            String[] people1 = people.get(index1).split(",");
            String[] people2 = people.get(index2).split(",");
            line.add(people1[0]);
            line.add(people1[3]);
            line.add(people1[5]);
            line.add(people2[0]);
            line.add(people2[3]);
            line.add(people2[5]);
            line.add(TimeGenerator.randomDateWithTrans(people1[4].compareTo(people2[4]) > 0 ? people1[4] : people2[4],
                    Generator.props.getProperty("phone.date.end"),
                    Generator.props.getProperty("phone.date.format")));
            line.add(TimeGenerator.randomTime());
            line.add(String.valueOf((int) (Math.random() * durationBase) + 1));
            result.add(line.toString());
        }
        return result.toString();
    }
}

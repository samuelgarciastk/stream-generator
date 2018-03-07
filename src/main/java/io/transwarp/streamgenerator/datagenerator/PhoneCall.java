package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.Consumer;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PhoneCall implements DataGen {
    private int windowSize;
    private double ratio;
    private Consumer consumer;

    public PhoneCall(Properties props) {
        windowSize = Integer.parseInt(props.getProperty("phone.window.size"));
        ratio = Double.parseDouble(props.getProperty("phone.ratio"));
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", windowSize);
        consumerProps.put("group.id", "PhoneCall");
        consumer = new Consumer(props.getProperty("people.topic"), consumerProps);
    }

    @Override
    public String nextRecord(int colNum) {
        List<String> people = consumer.getValues(windowSize);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < windowSize * ratio; i++) {
            List<String> line = new ArrayList<>();
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
            line.add(TimeGenerator.randomDate((people1[4].compareTo(people2[4]) > 0 ? people1[4] : people2[4]), "20171231", "yyyyMMdd"));
            line.add(TimeGenerator.randomTime());
            line.add(String.valueOf((int) (Math.random() * 60) + 1));
            result.add(line.stream().limit(colNum).collect(Collectors.joining(",")));
        }
        return result.stream().collect(Collectors.joining(System.getProperty("line.separator")));
    }
}

package io.transwarp.streamgenerator;

import io.transwarp.streamgenerator.columngenerator.RegexString;
import io.transwarp.streamgenerator.columngenerator.Timestamp;
import io.transwarp.streamgenerator.common.ConfLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class Generator {
    public static final Properties props = ConfLoader.loadProps("generator.properties");
    public static final String delimiter = props.getProperty("delimiter");
    public static final String topic = props.getProperty("topic");
    private static final int threadNum = Integer.parseInt(props.getProperty("thread.num"));
    private static final int dataPerSecond = Integer.parseInt(props.getProperty("data.per.second"));
    private List<DataGen> data = new ArrayList<>();

    public static void main(String[] args) {
        Generator generator = new Generator();
        generator.parseConf();
        generator.sendData();
    }

    public void parseConf() {
        String[] classes = props.getProperty("template").split("``");
        for (String name : classes) {
            name = name.trim();
            switch (name.substring(0, 2)) {
                case "T:": {
                    data.add(new Timestamp(name.substring(2)));
                    break;
                }
                case "R:": {
                    data.add(new RegexString(name.substring(2)));
                    break;
                }
                default: {
                    try {
                        data.add((DataGen) Class.forName("io.transwarp.streamgenerator.columngenerator." + name).getConstructor().newInstance());
                    } catch (ClassNotFoundException e) {
                        try {
                            data.add((DataGen) Class.forName("io.transwarp.streamgenerator.datagenerator." + name).getConstructor().newInstance());
                        } catch (Exception e1) {
                            System.out.println("Class not found: " + name);
                            e1.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String nextRecord() {
        StringJoiner record = new StringJoiner(delimiter);
        data.forEach(c -> record.add(c.nextRecord()));
        return record.toString();
    }

    public void sendData() {
        Sender.setIsPaused(true);
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) exec.submit(new Sender(Generator.this));
        exec.shutdown();

        long beginTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Sender.shutdown();
            System.out.println("Total messages sent: " + Sender.getSum());
            System.out.println("Total duration: " + (System.currentTimeMillis() - beginTime) / 1000.0);
        }));

        Sender.setIsPaused(false);
        long lastTime = System.currentTimeMillis();
        int limit = dataPerSecond - threadNum > 0 ? dataPerSecond - threadNum : 0;
        while (Sender.getIsStopped()) {
            if (Sender.getCount() < limit) continue;
            long now = System.currentTimeMillis();
            long diff = now - lastTime;
            lastTime = now;
            if (diff > 1000) continue;
            Sender.setIsPaused(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sender.resetCount();
            Sender.setIsPaused(false);
        }
    }
}

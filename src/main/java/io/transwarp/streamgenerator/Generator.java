package io.transwarp.streamgenerator;

import io.transwarp.streamgenerator.common.ConfLoader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
        String[] classes = props.getProperty("template").split(" ");
        for (String name : classes) {
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

    public String nextRecord() {
        StringJoiner record = new StringJoiner(delimiter);
        data.forEach(c -> record.add(c.nextRecord()));
        return record.toString();
    }

    public void sendData() {
        Properties props = ConfLoader.loadProps("producer.properties");
        Producer<String, String> producer = new KafkaProducer<>(props);
        AtomicInteger count = new AtomicInteger(0);
        Pause.INSTANCE.setIsPaused(true);

        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            exec.submit(() -> {
                while (true) {
                    if (Pause.INSTANCE.isPaused()) continue;
                    String msg = this.nextRecord();
                    if (msg.trim().equals("")) continue;
//                    producer.send(new ProducerRecord<>(topic, msg));
                    System.out.println(msg);
                    count.getAndIncrement();
                }
            });
        }
        exec.shutdown();

        Pause.INSTANCE.setIsPaused(false);
        long lastTime = System.currentTimeMillis();
        int limit = dataPerSecond - threadNum > 0 ? dataPerSecond - threadNum : 0;
        while (true) {
            if (count.get() < limit) continue;
            long now = System.currentTimeMillis();
            long diff = now - lastTime;
            lastTime = now;
            if (diff > 1000) continue;
            Pause.INSTANCE.setIsPaused(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count.set(0);
            Pause.INSTANCE.setIsPaused(false);
        }
    }
}

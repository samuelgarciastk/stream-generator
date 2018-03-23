package io.transwarp.streamcli;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/3/1
 * <p>
 * The entrance of stream-cli.
 * Load properties and configuration.
 * Assemble data and then send them.
 */
public class Generator {
    private Properties props;
    private String delimiter;
    private String topic;
    private int threadNum;
    private int dataPerSecond;
    private List<DataGen> data;

    public Generator() {
        props = ConfLoader.loadProps("generator.properties");
        delimiter = props.getProperty("delimiter");
        topic = props.getProperty("topic");
        threadNum = Integer.parseInt(props.getProperty("thread.num"));
        dataPerSecond = Integer.parseInt(props.getProperty("data.per.second"));
        data = new ArrayList<>();
    }

    public static void main(String[] args) {
        Generator generator = new Generator();
        generator.parseConf();
        generator.sendData(new AtomicBoolean(false));
    }

    /**
     * Parse the template in 'generator.properties'.
     */
    public void parseConf() {
        String[] columns = props.getProperty("template").split("``");
        for (String column : columns) {
            column = column.trim();
            if (column.contains("(") && column.contains(")")) {
                String name = column.substring(0, column.indexOf("("));
                String configString = column.substring(column.indexOf("(") + 1, column.lastIndexOf(")"));
                List<String> configs = null;
                if (configString.length() != 0)
                    configs = Arrays.stream(configString.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)).map(i -> i = i.substring(1, i.length() - 1)).collect(Collectors.toList());
                try {
                    data.add((DataGen) Class.forName("io.transwarp.streamcli.column." + name).getConstructor(List.class).newInstance(configs));
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                    System.err.println("Class not found: " + name);
                    e.printStackTrace();
                }
            } else {
                try {
                    data.add((DataGen) Class.forName("io.transwarp.streamcli.schema." + column).getConstructor(Properties.class).newInstance(props));
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e1) {
                    System.err.println("Class not found: " + column);
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Assemble data as one record.
     *
     * @return record String
     */
    public String nextRecord() {
        StringJoiner record = new StringJoiner(delimiter);
        data.forEach(c -> record.add(c.nextRecord()));
        return record.toString();
    }

    /**
     * Send data via Kafka.
     *
     * @param stopFlag flag from other classes to control whether to stop it.
     */
    public void sendData(AtomicBoolean stopFlag) {
        Properties producerProps = ConfLoader.loadProps("producer.properties");
        Producer<String, String> producer = new KafkaProducer<>(producerProps);
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger sum = new AtomicInteger(0);
        AtomicBoolean isPaused = new AtomicBoolean(true);
        AtomicBoolean isStopped = new AtomicBoolean(false);
        Boolean needPrint = props.getProperty("print.msg").equalsIgnoreCase("true");

        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++)
            exec.submit(() -> {
                while (!isStopped.get() && !stopFlag.get()) {
                    if (isPaused.get()) continue;
                    String msg = nextRecord();
                    if (msg.trim().equals("")) continue;
                    producer.send(new ProducerRecord<>(topic, msg));
                    if (needPrint) System.out.println(msg);
                    count.getAndIncrement();
                    sum.getAndIncrement();
                }
            });
        exec.shutdown();

        /*
        Handle Ctrl + C.
         */
        long beginTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            producer.close();
            isStopped.set(true);
            System.out.println("Total messages sent: " + sum.get());
            System.out.println("Total duration: " + (System.currentTimeMillis() - beginTime) / 1000.0);
        }));

        /*
        Control velocity.
         */
        isPaused.set(false);
        long lastTime = System.currentTimeMillis();
        int limit = dataPerSecond - threadNum > 0 ? dataPerSecond - threadNum : 0;
        while (!isStopped.get() && !stopFlag.get()) {
            if (count.get() < limit) continue;
            long now = System.currentTimeMillis();
            long diff = now - lastTime;
            lastTime = now;
            if (diff > 1000) continue;
            isPaused.set(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!needPrint) {
                String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                System.out.println("[" + time + "] sent a batch of messages.");
            }
            count.set(0);
            isPaused.set(false);
        }

        long now = System.currentTimeMillis();
        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.close();
        System.out.println("总发送量: " + sum.get());
        System.out.println("总耗时: " + (now - beginTime) / 1000.0);
        System.out.println("发送完成");
    }
}

package io.transwarp.streamgenerator;

import io.transwarp.streamgenerator.common.ConfLoader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: stk
 * Date: 2018/3/8
 */
public class Sender implements Runnable {
    private static final Properties props = ConfLoader.loadProps("producer.properties");
    private static final Producer<String, String> producer = new KafkaProducer<>(props);
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final AtomicInteger sum = new AtomicInteger(0);
    private static volatile boolean isPaused = true;
    private static volatile boolean isStopped = false;
    private Generator generator;

    public Sender(Generator generator) {
        this.generator = generator;
    }

    public static int getCount() {
        return count.get();
    }

    public static int getSum() {
        return sum.get();
    }

    public static boolean getIsStopped() {
        return isStopped;
    }

    public static void resetCount() {
        count.set(0);
    }

    public static void setIsPaused(boolean condition) {
        isPaused = condition;
    }

    public static void shutdown() {
        producer.close();
        isStopped = true;
    }

    @Override
    public void run() {
        while (!isStopped) {
            if (isPaused) continue;
            String msg = generator.nextRecord();
            if (msg.trim().equals("")) continue;
            producer.send(new ProducerRecord<>(Generator.topic, msg));
            System.out.println(msg);
            count.getAndIncrement();
            sum.getAndIncrement();
        }
    }
}

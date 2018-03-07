package io.transwarp.streamgenerator;

import io.transwarp.streamgenerator.common.ConfLoader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class Generator {
    public static void main(String[] args) {
        List<String> l = ConfLoader.loadConf("city", 1, 0);
        l.forEach(System.out::println);
//        Properties props = ConfLoader.loadProps("generator.properties");
//        DataGen dataGen = new PhoneCall(props);
//        new Generator().sendData(dataGen, props.getProperty("topic"),
//                Integer.parseInt(props.getProperty("thread.num")),
//                Integer.parseInt(props.getProperty("data.per.second")),
//                Integer.parseInt(props.getProperty("col.num")));
    }

    public void sendData(DataGen dataGen, String topic, int threadNum, int dataPerSecond) {
        Properties props = ConfLoader.loadProps("producer.properties");
        AtomicInteger count = new AtomicInteger(0);
        Pause.INSTANCE.setIsPaused(true);

        Producer<String, String> producer = new KafkaProducer<>(props);
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            exec.submit(() -> {
                while (true) {
                    if (Pause.INSTANCE.isPaused()) continue;
                    String msg = dataGen.nextRecord();
                    if (msg.trim().equals("")) continue;
//                    producer.send(new ProducerRecord<>(topic, msg));
                    System.out.println(msg);
                    count.getAndIncrement();
                }
            });
        }
        exec.shutdown();

        System.out.println("BEGIN");
        Pause.INSTANCE.setIsPaused(false);

        long lastTime = System.currentTimeMillis();
        while (true) {
            if (count.get() < dataPerSecond) continue;
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

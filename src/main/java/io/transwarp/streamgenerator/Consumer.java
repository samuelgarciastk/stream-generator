package io.transwarp.streamgenerator;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class Consumer {
    private KafkaConsumer<String, String> consumer;

    public Consumer(String topic, Properties props) {
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                consumer.seekToBeginning(partitions);
            }
        });
    }

    public String getOneValue() {
        String msg = "";
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                if (!record.value().equals("") && record.value() != null) {
                    msg = record.value();
                    break;
                }
            }
            if (!msg.equals("")) break;
        }
        return msg;
    }

    public List<String> getValues(int num) {
        List<String> msg = new ArrayList<>();
        int count = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                if (record.value().equals("") || record.value() == null) continue;
                msg.add(record.value());
                count++;
            }
            if (count >= num) break;
        }
        return msg;
    }
}

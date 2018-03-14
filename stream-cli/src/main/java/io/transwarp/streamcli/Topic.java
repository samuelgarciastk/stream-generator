package io.transwarp.streamcli;

import io.transwarp.streamcli.common.ConfLoader;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Author: stk
 * Date: 2018/3/12
 * <p>
 * Kafka topic manager.
 */
public class Topic {
    private Properties generatorProps;
    private AdminClient admin;

    public Topic() {
        generatorProps = ConfLoader.loadProps("generator.properties");
        Properties producerProps = ConfLoader.loadProps("producer.properties");
        Properties props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producerProps.getProperty("bootstrap.servers"));
        admin = AdminClient.create(props);
    }

    public boolean checkExist() {
        try {
            Set<String> topics = admin.listTopics().names().get();
            return topics.contains(generatorProps.getProperty("topic"));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("List topics error.");
        throw new NullPointerException();
    }

    public boolean createTopic() {
        String topic = generatorProps.getProperty("topic");
        int partition = Integer.parseInt(generatorProps.getProperty("partition.num"));
        short replication = Short.parseShort(generatorProps.getProperty("replication.num"));
        CreateTopicsResult result = admin.createTopics(Collections.singletonList(new NewTopic(topic, partition, replication)));
        try {
            result.all().get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Topic " + topic + " already exists.");
            return false;
        }
        return true;
    }

    public void close() {
        admin.close();
    }
}

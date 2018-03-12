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
    private static final Properties props = ConfLoader.loadProps("producer.properties");
    private AdminClient admin;

    public Topic() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("bootstrap.servers"));
        admin = AdminClient.create(props);
    }

    public Set<String> listTopics() {
        try {
            return admin.listTopics().names().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("List topics error.");
        throw new NullPointerException();
    }

    public boolean createTopic() {
        int partition = Integer.parseInt(Generator.props.getProperty("partition.num"));
        short replication = Short.parseShort(Generator.props.getProperty("replication.num"));
        CreateTopicsResult result = admin.createTopics(Collections.singletonList(new NewTopic(Generator.topic, partition, replication)));
        try {
            result.all().get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Topic " + Generator.topic + " already exists.");
            return false;
        }
        return true;
    }

    public void close() {
        admin.close();
    }
}

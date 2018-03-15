package io.transwarp.streamgui;

import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.Topic;
import io.transwarp.streamcli.common.ConfLoader;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: stk
 * Date: 2018/3/12
 */
public class SenderPanel extends JPanel {
    private Properties generatorProps;
    private Properties producerProps;
    private AtomicBoolean stopFlag;
    private JPanel basicPane;
    private Box advancedPane;
    private JComboBox<String> template;
    private List<JTextField> basic;
    private List<JTextField> advanced;
    private JButton start;
    private JButton stop;

    public SenderPanel() {
        generatorProps = ConfLoader.loadProps("generator.properties");
        producerProps = ConfLoader.loadProps("producer.properties");
        stopFlag = new AtomicBoolean(false);
        setLayout(new BorderLayout());
        basicPane = new JPanel(new BorderLayout());
        initTemplatePane();
        initControlPane();
        add(basicPane, BorderLayout.CENTER);
        initAdvancedPane();
        setVisible(true);
    }

    private void initTemplatePane() {
        Template currentTemplate = Template.valueOf(generatorProps.getProperty("template"));

        JTextArea desc = new JTextArea();
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBackground(null);
        desc.setText(currentTemplate.getDesc());

        template = new JComboBox<>();
        setFixedSize(template, new Dimension(200, 30));
        Arrays.stream(Template.values()).forEach(i -> template.addItem(i.getName()));
        template.setSelectedItem(currentTemplate.getName());
        template.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                desc.setText(Template.getEnum(String.valueOf(e.getItem())).getDesc());
            }
        });

        JButton testConn = new JButton("测试连接");
        testConn.setFocusPainted(false);
        setFixedSize(testConn, new Dimension(90, 30));
        testConn.addActionListener(e -> new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                Properties props = new Properties();
                props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, basic.get(0).getText());
                try (AdminClient adminClient = AdminClient.create(props)) {
                    adminClient.listTopics(new ListTopicsOptions().timeoutMs(5000)).listings().get();
                    return true;
                } catch (InterruptedException | ExecutionException e1) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    if (get())
                        JOptionPane.showMessageDialog(null, "连接成功", "测试连接", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "无法连接", "测试连接", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        }.execute());

        basic = new ArrayList<>();
        basic.add(new JTextField(producerProps.getProperty("bootstrap.servers")));
        basic.add(new JTextField(generatorProps.getProperty("topic")));
        basic.forEach(i -> setFixedSize(i, new Dimension(200, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("Servers："));
        labels.add(new JLabel("Topic："));
        labels.add(new JLabel("数据模板："));
        labels.add(new JLabel("模板内容："));
        labels.forEach(i -> setFixedSize(i, new Dimension(100, 30)));

        Box box_servers = Box.createHorizontalBox();
        box_servers.add(labels.get(0));
        box_servers.add(Box.createHorizontalStrut(10));
        box_servers.add(basic.get(0));
        box_servers.add(Box.createHorizontalStrut(10));
        box_servers.add(testConn);
        box_servers.add(Box.createHorizontalGlue());

        Box box_topic = Box.createHorizontalBox();
        box_topic.add(labels.get(1));
        box_topic.add(Box.createHorizontalStrut(10));
        box_topic.add(basic.get(1));
        box_topic.add(Box.createHorizontalGlue());

        Box box_template = Box.createHorizontalBox();
        box_template.add(labels.get(2));
        box_template.add(Box.createHorizontalStrut(10));
        box_template.add(template);
        box_template.add(Box.createHorizontalGlue());

        Box box_content = Box.createHorizontalBox();
        box_content.add(labels.get(3));
        box_content.add(Box.createHorizontalGlue());

        Box templatePane = Box.createVerticalBox();
        templatePane.setBorder(new EmptyBorder(20, 20, 20, 20));
        templatePane.add(box_servers);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(box_topic);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(box_template);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(box_content);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(desc);
        basicPane.add(templatePane, BorderLayout.CENTER);
    }

    private void initControlPane() {
        start = new JButton("开始");
        stop = new JButton("停止");
        JButton advanced = new JButton("<<");
        start.setFocusPainted(false);
        stop.setFocusPainted(false);
        advanced.setFocusPainted(false);
        start.setEnabled(true);
        stop.setEnabled(false);

        start.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                setAbleToStart(false);
                stopFlag.set(false);
                genProps();
                send();
                return null;
            }
        }.execute());
        stop.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            stopFlag.set(true);
            setAbleToStart(true);
        }));
        advanced.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            if (advanced.getText().equals("<<")) {
                advanced.setText(">>");
                advancedPane.setVisible(true);
            } else {
                advanced.setText("<<");
                advancedPane.setVisible(false);
            }
            updateUI();
        }));

        Box controlPane = Box.createHorizontalBox();
        controlPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        controlPane.add(Box.createHorizontalStrut(20));
        controlPane.add(start);
        controlPane.add(Box.createHorizontalStrut(20));
        controlPane.add(stop);
        controlPane.add(Box.createHorizontalGlue());
        controlPane.add(advanced);
        controlPane.add(Box.createHorizontalStrut(20));
        basicPane.add(controlPane, BorderLayout.SOUTH);
    }

    private void initAdvancedPane() {
        advanced = new ArrayList<>();
        advanced.add(new JTextField(generatorProps.getProperty("thread.num")));
        advanced.add(new JTextField(generatorProps.getProperty("delimiter")));
        advanced.add(new JTextField(generatorProps.getProperty("data.per.second")));
        advanced.add(new JTextField(generatorProps.getProperty("partition.num")));
        advanced.add(new JTextField(generatorProps.getProperty("replication.num")));
        advanced.forEach(i -> setFixedSize(i, new Dimension(90, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("线程数量："));
        labels.add(new JLabel("分隔符："));
        labels.add(new JLabel("发送速率(条/秒)："));
        labels.add(new JLabel("Topic分区数量："));
        labels.add(new JLabel("Topic副本数量："));
        labels.forEach(i -> setFixedSize(i, new Dimension(120, 30)));

        List<Box> lines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Box line = Box.createHorizontalBox();
            line.add(labels.get(i));
            line.add(Box.createHorizontalStrut(10));
            line.add(advanced.get(i));
            lines.add(line);
        }

        advancedPane = Box.createVerticalBox();
        advancedPane.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Advanced"), new EmptyBorder(10, 10, 10, 10)));
        lines.forEach(i -> {
            advancedPane.add(i);
            advancedPane.add(Box.createVerticalStrut(20));
        });
        advancedPane.setVisible(false);
        add(advancedPane, BorderLayout.EAST);
    }

    private void setAbleToStart(boolean state) {
        start.setEnabled(state);
        stop.setEnabled(!state);
    }

    private void setFixedSize(JComponent component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    private void send() {
        Topic topicTool = new Topic();
        if (!topicTool.checkExist()) {
            if (!topicTool.createTopic()) {
                JOptionPane.showMessageDialog(null, "无法创建Topic，请稍后重试。", "Topic创建失败", JOptionPane.ERROR_MESSAGE);
                setAbleToStart(true);
                return;
            }
            System.out.println("Topic created.");
        }
        System.out.println("Topic exists.");
        topicTool.close();
        Generator generator = new Generator();
        generator.parseConf();
        generator.sendData(stopFlag);
    }

    private void genProps() {
        String generatorFile = "generator.properties";
        String producerFile = "producer.properties";
        Properties generatorProps = ConfLoader.loadProps(generatorFile);
        Properties producerProps = ConfLoader.loadProps(producerFile);

        generatorProps.setProperty("topic", basic.get(1).getText());
        generatorProps.setProperty("template", Template.getEnum(String.valueOf(template.getSelectedItem())).toString());
        generatorProps.setProperty("thread.num", advanced.get(0).getText());
        generatorProps.setProperty("delimiter", advanced.get(1).getText());
        generatorProps.setProperty("data.per.second", advanced.get(2).getText());
        generatorProps.setProperty("partition.num", advanced.get(3).getText());
        generatorProps.setProperty("replication.num", advanced.get(4).getText());

        producerProps.setProperty("bootstrap.servers", basic.get(0).getText());

        ConfLoader.writeProps(generatorFile, generatorProps);
        ConfLoader.writeProps(producerFile, producerProps);
    }
}

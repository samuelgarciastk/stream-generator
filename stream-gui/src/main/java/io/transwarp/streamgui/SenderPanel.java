package io.transwarp.streamgui;

import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.ConfLoader;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: stk
 * Date: 2018/3/12
 */
public class SenderPanel extends JPanel {
    private static final Map<String, String> templateName = ConfLoader.loadMap("template");
    private JPanel basicPane;
    private Box advancedPane;
    private JTextField topic;
    private JComboBox<String> template;
    private List<JTextField> advanced;

    public SenderPanel() {
        setLayout(new BorderLayout());
        basicPane = new JPanel(new BorderLayout());
        initTemplatePane();
        initControlPane();
        add(basicPane, BorderLayout.CENTER);
        initAdvancedPane();
        setVisible(true);
    }

    private void initTemplatePane() {
        JTextArea desc = new JTextArea();
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setText(templateName.entrySet().iterator().next().getValue().split(";")[1]);

        template = new JComboBox<>();
        setFixedSize(template, new Dimension(200, 30));
        templateName.forEach((k, v) -> template.addItem(k));
        template.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                desc.setText(templateName.get(String.valueOf(e.getItem())).split(";")[1]);
            }
        });

        topic = new JTextField(Generator.props.getProperty("topic"));
        setFixedSize(topic, new Dimension(200, 30));

        JLabel label_topic = new JLabel("Topic：");
        JLabel label_template = new JLabel("数据模板：");
        JLabel label_content = new JLabel("模板内容：");
        setFixedSize(label_topic, new Dimension(100, 30));
        setFixedSize(label_template, new Dimension(100, 30));
        setFixedSize(label_content, new Dimension(100, 30));

        Box box_topic = Box.createHorizontalBox();
        box_topic.add(label_topic);
        box_topic.add(Box.createHorizontalStrut(10));
        box_topic.add(topic);
        box_topic.add(Box.createHorizontalGlue());

        Box box_template = Box.createHorizontalBox();
        box_template.add(label_template);
        box_template.add(Box.createHorizontalStrut(10));
        box_template.add(template);
        box_template.add(Box.createHorizontalGlue());

        Box box_content = Box.createHorizontalBox();
        box_content.add(label_content);
        box_content.add(Box.createHorizontalGlue());

        Box templatePane = Box.createVerticalBox();
        templatePane.setBorder(new EmptyBorder(20, 20, 20, 20));
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
        JButton start = new JButton("开始");
        JButton stop = new JButton("停止");
        JButton advanced = new JButton("<<");
        start.setFocusPainted(false);
        stop.setFocusPainted(false);
        advanced.setFocusPainted(false);
        start.setEnabled(true);
        stop.setEnabled(false);

        start.addActionListener(e -> {
            start.setEnabled(false);
            stop.setEnabled(true);
            System.out.println("Begin");
        });

        stop.addActionListener(e -> {
            stop.setEnabled(false);
            start.setEnabled(true);
            System.out.println("Stop");
        });

        advanced.addActionListener(e -> {
            if (advanced.getText().equals("<<")) {
                advanced.setText(">>");
                advancedPane.setVisible(true);
            } else {
                advanced.setText("<<");
                advancedPane.setVisible(false);
            }
            updateUI();
        });

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
        advanced.add(new JTextField(Generator.props.getProperty("thread.num")));
        advanced.add(new JTextField(Generator.props.getProperty("delimiter")));
        advanced.add(new JTextField(Generator.props.getProperty("data.per.second")));
        advanced.add(new JTextField(Generator.props.getProperty("partition.num")));
        advanced.add(new JTextField(Generator.props.getProperty("replication.num")));
        advanced.forEach(i -> setFixedSize(i, new Dimension(60, 30)));

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

    private void setFixedSize(JComponent component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }
}

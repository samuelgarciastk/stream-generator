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
        templateName.forEach((k, v) -> template.addItem(k));
        template.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                desc.setText(templateName.get(String.valueOf(e.getItem())).split(";")[1]);
            }
        });

        JLabel label1 = new JLabel("数据模板：");
        JLabel label2 = new JLabel("模板内容：");

        Box line1 = Box.createHorizontalBox();
        line1.add(label1);
        line1.add(Box.createHorizontalStrut(10));
        line1.add(template);
        line1.add(Box.createHorizontalGlue());

        Box line2 = Box.createHorizontalBox();
        line2.add(label2);
        line2.add(Box.createHorizontalGlue());

        Box templatePane = Box.createVerticalBox();
        templatePane.setBorder(new EmptyBorder(20, 20, 20, 20));
        templatePane.add(line1);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(line2);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(desc);
        basicPane.add(templatePane, BorderLayout.CENTER);
    }

    private void initControlPane() {
        JButton start = new JButton("开始");
        start.setFocusPainted(false);
        JButton stop = new JButton("停止");
        stop.setFocusPainted(false);
        JButton advanced = new JButton("<<");
        advanced.setFocusPainted(false);

        start.addActionListener(e -> {
            start.setForeground(Color.WHITE);
            start.setBackground(Color.RED);
            stop.setForeground(null);
            stop.setBackground(null);
            System.out.println("Begin");
        });

        stop.addActionListener(e -> {
            stop.setForeground(Color.WHITE);
            stop.setBackground(Color.RED);
            start.setForeground(null);
            start.setBackground(null);
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
        advanced.forEach(i -> i.setPreferredSize(new Dimension(50, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("线程数量："));
        labels.add(new JLabel("分隔符："));
        labels.add(new JLabel("发送速率(条/秒)："));
        labels.add(new JLabel("Topic分区数量："));
        labels.add(new JLabel("Topic副本数量："));
        labels.forEach(i -> i.setPreferredSize(new Dimension(120, 30)));

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
}

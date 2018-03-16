package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamgui.common.UITools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class CoreConfigPanel extends PropsPanel {
    private List<JTextField> advancedField;

    public CoreConfigPanel() {
        Properties generatorProps = ConfLoader.loadProps("generator.properties");
        advancedField = new ArrayList<>();
        advancedField.add(new JTextField(generatorProps.getProperty("thread.num")));
        advancedField.add(new JTextField(generatorProps.getProperty("delimiter")));
        advancedField.add(new JTextField(generatorProps.getProperty("data.per.second")));
        advancedField.add(new JTextField(generatorProps.getProperty("partition.num")));
        advancedField.add(new JTextField(generatorProps.getProperty("replication.num")));
        advancedField.forEach(i -> UITools.setFixedSize(i, new Dimension(100, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("线程数量："));
        labels.add(new JLabel("分隔符："));
        labels.add(new JLabel("发送速率(条/秒)："));
        labels.add(new JLabel("Topic分区数量："));
        labels.add(new JLabel("Topic副本数量："));
        labels.forEach(i -> UITools.setFixedSize(i, new Dimension(180, 30)));

        List<Box> lines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Box line = Box.createHorizontalBox();
            line.add(labels.get(i));
            line.add(advancedField.get(i));
            lines.add(line);
        }

        Box advancedPane = Box.createVerticalBox();
        lines.forEach(i -> {
            advancedPane.add(i);
            advancedPane.add(Box.createVerticalStrut(20));
        });
        setLayout(new BorderLayout());
        add(advancedPane, BorderLayout.CENTER);
    }

    @Override
    public Properties genProps() {
        Properties props = new Properties();
        props.setProperty("thread.num", advancedField.get(0).getText());
        props.setProperty("delimiter", advancedField.get(1).getText());
        props.setProperty("data.per.second", advancedField.get(2).getText());
        props.setProperty("partition.num", advancedField.get(3).getText());
        props.setProperty("replication.num", advancedField.get(4).getText());
        return props;
    }
}

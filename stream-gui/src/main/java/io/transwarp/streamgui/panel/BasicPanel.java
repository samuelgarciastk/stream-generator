package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamgui.config.Template;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.transwarp.streamgui.common.UITools.setFixedSize;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class BasicPanel extends PropsPanel {
    private JComboBox<String> template;
    private List<JTextField> basicField;

    public BasicPanel() {
        Properties generatorProps = ConfLoader.loadProps("generator.properties");
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
                SwingUtilities.invokeLater(this::addCustomPane);
            }
        });

        basicField = new ArrayList<>();
        basicField.add(new JTextField(generatorProps.getProperty("topic")));
        basicField.forEach(i -> setFixedSize(i, new Dimension(200, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("Topic："));
        labels.add(new JLabel("数据模板："));
        labels.add(new JLabel("模板内容："));
        labels.forEach(i -> setFixedSize(i, new Dimension(100, 30)));

        Box box_topic = Box.createHorizontalBox();
        box_topic.add(labels.get(1));
        box_topic.add(Box.createHorizontalStrut(10));
        box_topic.add(basicField.get(1));
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
        templatePane.add(box_topic);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(box_template);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(box_content);
        templatePane.add(Box.createVerticalStrut(10));
        templatePane.add(desc);
        add(templatePane, BorderLayout.CENTER);
    }

    private void addCustomPane() {

    }

    @Override
    public Properties genProps() {
        return null;
    }
}

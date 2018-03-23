package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamgui.common.UITools;
import io.transwarp.streamgui.config.Template;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class BasicPanel extends PropsBox {
    private JScrollPane scrollPane;
    private PropsBox advancedPane;
    private JComboBox<String> template;
    private List<JTextField> basicField;

    public BasicPanel(JScrollPane scrollPane) {
        super(BoxLayout.Y_AXIS);
        try {
            ((Box) scrollPane.getViewport().getView()).remove(1);
        } catch (Exception ignored) {
        }
        this.scrollPane = scrollPane;
        Properties generatorProps = ConfLoader.loadProps("generator.properties");
        Template currentTemplate;
        try {
            currentTemplate = Template.valueOf(generatorProps.getProperty("template"));
        } catch (IllegalArgumentException e) {
            currentTemplate = Template.BankAccount;
        }

        JTextArea desc = new JTextArea();
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBackground(null);
        desc.setText(currentTemplate.getDesc());

        template = new JComboBox<>();
        UITools.setFixedSize(template, new Dimension(200, 30));
        Arrays.stream(Template.values()).forEach(i -> template.addItem(i.getName()));
        template.setSelectedItem(currentTemplate.getName());
        template.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(() -> desc.setText(Template.getEnum(String.valueOf(e.getItem())).getDesc()));
                changeAdvancedPane();
            }
        });

        basicField = new ArrayList<>();
        basicField.add(new JTextField(generatorProps.getProperty("topic")));
        basicField.forEach(i -> UITools.setFixedSize(i, new Dimension(200, 30)));

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("Topic："));
        labels.add(new JLabel("数据模板："));
        labels.add(new JLabel("模板内容："));
        labels.forEach(i -> UITools.setFixedSize(i, new Dimension(100, 30)));

        Box box_topic = Box.createHorizontalBox();
        box_topic.add(labels.get(0));
        box_topic.add(Box.createHorizontalStrut(10));
        box_topic.add(basicField.get(0));
        box_topic.add(Box.createHorizontalGlue());

        Box box_template = Box.createHorizontalBox();
        box_template.add(labels.get(1));
        box_template.add(Box.createHorizontalStrut(10));
        box_template.add(template);
        box_template.add(Box.createHorizontalGlue());

        Box box_content = Box.createHorizontalBox();
        box_content.add(labels.get(2));
        box_content.add(Box.createHorizontalGlue());

        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        add(box_topic);
        add(Box.createVerticalStrut(10));
        add(box_template);
        add(Box.createVerticalStrut(10));
        add(box_content);
        add(Box.createVerticalStrut(10));
        add(desc);
        changeAdvancedPane();
    }

    private void changeAdvancedPane() {
        SwingUtilities.invokeLater(() -> {
            Box sidePane = (Box) scrollPane.getViewport().getView();
            if (advancedPane != null) sidePane.remove(advancedPane);
            advancedPane = new AdvancedPanel(String.valueOf(template.getSelectedItem()));
            sidePane.add(advancedPane);
            scrollPane.validate();
            scrollPane.repaint();
        });
    }

    @Override
    public Properties genProps() {
        Properties props = new Properties();
        props.setProperty("topic", basicField.get(0).getText().trim());
        props.setProperty("template", Template.getEnum(String.valueOf(template.getSelectedItem())).toString());
        Properties advancedProps = advancedPane.genProps();
        props.putAll(advancedProps);
        return props;
    }
}

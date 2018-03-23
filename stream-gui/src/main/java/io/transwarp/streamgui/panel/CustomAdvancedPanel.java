package io.transwarp.streamgui.panel;

import io.transwarp.streamgui.common.UITools;
import io.transwarp.streamgui.config.Column;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/20
 */
public class CustomAdvancedPanel extends PropsBox {
    private String column;
    private List<JTextField> customField;

    CustomAdvancedPanel(String column) {
        super(BoxLayout.Y_AXIS);
        this.column = column;
        List<String> configs = Column.getEnum(column).getConfigs();
        if (configs != null) {
            customField = new ArrayList<>();
            List<JLabel> labels = new ArrayList<>();
            configs.forEach(i -> {
                labels.add(new JLabel(i + "："));
                customField.add(new JTextField());
            });
            labels.forEach(i -> UITools.setFixedSize(i, new Dimension(180, 30)));
            customField.forEach(i -> UITools.setFixedSize(i, new Dimension(100, 30)));

            List<Box> lines = new ArrayList<>();
            for (int i = 0; i < configs.size(); i++) {
                Box line = Box.createHorizontalBox();
                line.add(labels.get(i));
                line.add(customField.get(i));
                lines.add(line);
            }

            lines.forEach(i -> {
                add(i);
                add(Box.createVerticalStrut(20));
            });
        }
    }

    @Override
    public Properties genProps() {
        Properties props = new Properties();
        StringJoiner configString = new StringJoiner(",");
        customField.forEach(i -> configString.add("\"" + i.getText().trim() + "\""));
        props.setProperty("column", column + configString.toString());
        return props;
    }
}

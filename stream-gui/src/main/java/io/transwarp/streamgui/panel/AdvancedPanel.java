package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamgui.common.UITools;
import io.transwarp.streamgui.config.Template;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class AdvancedPanel extends PropsBox {
    private List<String> customConfigs;
    private List<JTextField> customField;

    AdvancedPanel(String template) {
        super(BoxLayout.Y_AXIS);
        customConfigs = Template.getEnum(template).getConfig();
        if (customConfigs != null) {
            Properties generatorProps = ConfLoader.loadProps("generator.properties");
            customField = new ArrayList<>();
            List<JLabel> labels = new ArrayList<>();
            customConfigs.forEach(i -> {
                labels.add(new JLabel(i + "："));
                customField.add(new JTextField(generatorProps.getProperty(i)));
            });
            labels.forEach(i -> UITools.setFixedSize(i, new Dimension(180, 30)));
            customField.forEach(i -> UITools.setFixedSize(i, new Dimension(100, 30)));

            List<Box> lines = new ArrayList<>();
            for (int i = 0; i < customConfigs.size(); i++) {
                Box line = Box.createHorizontalBox();
                line.add(labels.get(i));
                line.add(customField.get(i));
                lines.add(line);
            }

            lines.forEach(i -> {
                add(i);
                add(Box.createVerticalStrut(20));
            });
        } else {
            customConfigs = new ArrayList<>();
        }
    }

    @Override
    public Properties genProps() {
        Properties props = new Properties();
        for (int i = 0; i < customConfigs.size(); i++)
            props.setProperty(customConfigs.get(i), customField.get(i).getText());
        return props;
    }
}

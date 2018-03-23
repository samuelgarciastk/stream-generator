package io.transwarp.streamgui.panel;

import io.transwarp.streamgui.common.UITools;
import io.transwarp.streamgui.common.WrapLayout;
import io.transwarp.streamgui.config.Column;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class CustomPanel extends PropsBox {
    private JScrollPane scrollPane;
    private PropsBox advancedPane;
    private JTextArea desc;
    private List<JComboBox<String>> columns;
    private List<PropsBox> customPanes;

    public CustomPanel(JScrollPane scrollPane) {
        super(BoxLayout.Y_AXIS);
        try {
            ((Box) scrollPane.getViewport().getView()).remove(1);
        } catch (Exception ignored) {
        }
        this.scrollPane = scrollPane;
        JPanel panel = new JPanel(new BorderLayout());

        Box preview = Box.createVerticalBox();
        Box label_line = Box.createHorizontalBox();
        JLabel label = new JLabel("模板预览：");
        UITools.setFixedSize(label, new Dimension(100, 30));
        label_line.add(label);
        label_line.add(Box.createHorizontalGlue());
        desc = new JTextArea(5, 0);
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBackground(null);
        desc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        preview.add(label_line);
        preview.add(Box.createVerticalStrut(10));
        preview.add(new JScrollPane(desc));
        panel.add(preview, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new WrapLayout(FlowLayout.LEFT));
        columns = new ArrayList<>();
        columns.add(getColumn());
        buttonPanel.add(columns.get(0));
        buttonPanel.add(Box.createHorizontalStrut(10));
        desc.setText(String.valueOf(columns.get(0).getSelectedItem()));

        JButton add = new JButton("+");
        add.setFocusPainted(false);
        UITools.setFixedSize(add, new Dimension(45, 30));
        buttonPanel.add(add);
        JButton remove = new JButton("-");
        remove.setFocusPainted(false);
        UITools.setFixedSize(remove, new Dimension(45, 30));
        buttonPanel.add(remove);

        JScrollPane scroll = new JScrollPane(buttonPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setMinimumSize(new Dimension(0, 250));
        add(panel);

        add.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            buttonPanel.remove(remove);
            buttonPanel.remove(add);
            columns.add(getColumn());
            buttonPanel.add(columns.get(columns.size() - 1));
            buttonPanel.add(Box.createHorizontalStrut(10));
            buttonPanel.add(add);
            buttonPanel.add(remove);
            setTemplate();
            scroll.validate();
            scroll.repaint();
        }));

        remove.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            buttonPanel.remove(remove);
            buttonPanel.remove(add);
            buttonPanel.remove(columns.get(columns.size() - 1));
            columns.remove(columns.size() - 1);
            buttonPanel.remove(buttonPanel.getComponentCount() - 1);
            buttonPanel.add(add);
            buttonPanel.add(remove);
            setTemplate();
            scroll.validate();
            scroll.repaint();
        }));
    }

    private JComboBox<String> getColumn() {
        JComboBox<String> column = new JComboBox<>();
        Arrays.stream(Column.values()).forEach(i -> column.addItem(i.getName()));
        UITools.setFixedSize(column, new Dimension(100, 30));
        column.addItemListener(e -> {
            setTemplate();
            changeAdvancedPane(String.valueOf(column.getSelectedItem()));
        });
        return column;
    }

    private void setTemplate() {
        SwingUtilities.invokeLater(() -> {
            StringJoiner template = new StringJoiner(" | ");
            columns.forEach(i -> template.add(String.valueOf(i.getSelectedItem())));
            desc.setText(template.toString());
        });
    }

    private void changeAdvancedPane(String column) {
        SwingUtilities.invokeLater(() -> {
            Box sidePane = (Box) scrollPane.getViewport().getView();
            if (advancedPane != null) sidePane.remove(advancedPane);
            advancedPane = new CustomAdvancedPanel(column);
            sidePane.add(advancedPane);
            scrollPane.validate();
            scrollPane.repaint();
        });
    }

    @Override
    public Properties genProps() {
        StringJoiner template = new StringJoiner("``");
        columns.forEach(i -> template.add(Column.getEnum(String.valueOf(i.getSelectedItem())).toString()));
        Properties props = new Properties();
        props.setProperty("template", template.toString());
        return props;
    }
}

package io.transwarp.streamgui.panel;

import io.transwarp.streamgui.common.UITools;
import io.transwarp.streamgui.common.WrapLayout;
import io.transwarp.streamgui.config.Column;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class CustomPanel extends PropsBox {
    private JScrollPane scrollPane;
    private PropsBox advancedPane;
    private JTextArea desc;
    private JPanel buttonPanel;
    private JScrollPane scroll;
    private LinkedHashMap<JComboBox<String>, PropsBox> columnMap;

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

        buttonPanel = new JPanel(new WrapLayout(FlowLayout.LEFT));
        columnMap = new LinkedHashMap<>();
        addColumn();

        JButton add = new JButton("+");
        JButton remove = new JButton("-");
        add.setFocusPainted(false);
        remove.setFocusPainted(false);
        UITools.setFixedSize(add, new Dimension(45, 30));
        UITools.setFixedSize(remove, new Dimension(45, 30));
        add.addActionListener(e -> addColumn());
        remove.addActionListener(e -> removeColumn());
        buttonPanel.add(add);
        buttonPanel.add(remove);

        scroll = new JScrollPane(buttonPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setMinimumSize(new Dimension(0, 250));
        add(panel);
    }

    private void addColumn() {
        JComboBox<String> column = new JComboBox<>();
        Arrays.stream(Column.values()).forEach(i -> column.addItem(i.getName()));
        UITools.setFixedSize(column, new Dimension(100, 30));
        PropsBox pane = new CustomAdvancedPanel(String.valueOf(column.getSelectedItem()));
        columnMap.put(column, pane);
        column.addItemListener(e -> changeColumn(column));
        column.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                changeColumn(column);
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        SwingUtilities.invokeLater(() -> {
            buttonPanel.add(column, buttonPanel.getComponentCount() - 2);
            buttonPanel.add(Box.createHorizontalStrut(10), buttonPanel.getComponentCount() - 2);
            setTemplate();
            scroll.validate();
            scroll.repaint();
        });
        changeAdvancedPane(pane);
    }

    private void removeColumn() {
        if (buttonPanel.getComponentCount() < 5) return;
        SwingUtilities.invokeLater(() -> {
            buttonPanel.remove(buttonPanel.getComponentCount() - 3);
            buttonPanel.remove(buttonPanel.getComponentCount() - 3);
            setTemplate();
            scroll.validate();
            scroll.repaint();
        });
        columnMap.remove(getLast().getKey());
        changeAdvancedPane(getLast().getValue());
    }

    private void changeColumn(JComboBox<String> column) {
        setTemplate();
        PropsBox currentPane = columnMap.get(column);
        String selectedName = String.valueOf(column.getSelectedItem());
        if (!selectedName.equals(((CustomAdvancedPanel) currentPane).getColumn())) {
            currentPane = new CustomAdvancedPanel(selectedName);
            columnMap.put(column, currentPane);
        }
        changeAdvancedPane(currentPane);
    }

    private void changeAdvancedPane(PropsBox pane) {
        SwingUtilities.invokeLater(() -> {
            Box sidePane = (Box) scrollPane.getViewport().getView();
            if (advancedPane != null) sidePane.remove(advancedPane);
            advancedPane = pane;
            sidePane.add(advancedPane);
            scrollPane.validate();
            scrollPane.repaint();
        });
    }

    private void setTemplate() {
        SwingUtilities.invokeLater(() -> {
            StringJoiner template = new StringJoiner(" | ");
            columnMap.forEach((k, v) -> template.add(String.valueOf(k.getSelectedItem())));
            desc.setText(template.toString());
        });
    }

    private Map.Entry<JComboBox<String>, PropsBox> getLast() {
        Iterator<Map.Entry<JComboBox<String>, PropsBox>> iterator = columnMap.entrySet().iterator();
        Map.Entry<JComboBox<String>, PropsBox> last = null;
        while (iterator.hasNext()) last = iterator.next();
        return last;
    }

    @Override
    public Properties genProps() {
        StringJoiner template = new StringJoiner("``");
        columnMap.forEach((k, v) -> template.add(v.genProps().getProperty("column")));
        Properties props = new Properties();
        props.setProperty("template", template.toString());
        return props;
    }
}

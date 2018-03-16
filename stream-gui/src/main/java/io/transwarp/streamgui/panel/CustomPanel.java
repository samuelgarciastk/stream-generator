package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class CustomPanel extends JPanel {
    private Properties generatorProps;
    private Properties producerProps;
    private AtomicBoolean stopFlag;
    private List<String> columns;
    private JPanel basicPane;
    private List<JTextField> basicField;
    private JButton append;
    private JButton start;
    private JButton stop;

    public CustomPanel() {
        generatorProps = ConfLoader.loadProps("generator.properties");
        producerProps = ConfLoader.loadProps("producer.properties");
        stopFlag = new AtomicBoolean(false);
        setLayout(new BorderLayout());
        basicPane = new JPanel(new BorderLayout());
        initCustomPane();
        initControlPane();
        add(basicPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void initCustomPane() {
        columns = new ArrayList<>();
        append = new JButton("添加");
        append.setFocusPainted(false);
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
                return null;
            }
        }.execute());
        stop.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            stopFlag.set(true);
            setAbleToStart(true);
        }));

        Box controlPane = Box.createHorizontalBox();
        controlPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        controlPane.add(Box.createHorizontalStrut(20));
        controlPane.add(start);
        controlPane.add(Box.createHorizontalStrut(20));
        controlPane.add(stop);
        add(controlPane, BorderLayout.SOUTH);
    }

    private void setAbleToStart(boolean state) {
        start.setEnabled(state);
        stop.setEnabled(!state);
    }
}

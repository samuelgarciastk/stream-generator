package io.transwarp.streamgui.panel;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamgui.common.UITools;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class ControlPanel extends JPanel {
    private Properties producerProps;
    private AtomicBoolean stopFlag;
    private JPanel centerPane;
    private PropsPanel mainPane;
    private Box sidePane;
    private PropsPanel advancedPane;
    private JScrollPane scrollPane;
    private JTextField testConnField;
    private JButton start;
    private JButton stop;

    public ControlPanel() {
        producerProps = ConfLoader.loadProps("producer.properties");
        stopFlag = new AtomicBoolean(false);
        setLayout(new BorderLayout());

        centerPane = new JPanel(new BorderLayout());
//        mainPane = new BasicPanel();
//        centerPane.add(mainPane, BorderLayout.CENTER);
        initControlPane();
        add(centerPane, BorderLayout.CENTER);

        sidePane = Box.createVerticalBox();
        sidePane.add(new CoreConfigPanel());
        advancedPane = new AdvancedPanel();
        sidePane.add(advancedPane);

        scrollPane = new JScrollPane(sidePane);
        scrollPane.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("高级"), new EmptyBorder(10, 10, 10, 0)));
        UITools.setFixedSize(scrollPane, new Dimension(350, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setVisible(false);
        add(scrollPane, BorderLayout.EAST);
    }

    private void initControlPane() {
        JLabel testConnLabel = new JLabel("Servers：");
        testConnField = new JTextField(producerProps.getProperty("bootstrap.servers"));
        JButton testConn = new JButton("测试连接");
        testConn.setFocusPainted(false);
        UITools.setFixedSize(testConn, new Dimension(90, 30));
        testConn.addActionListener(e -> new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                Properties props = new Properties();
                props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, testConnField.getText());
                try (AdminClient adminClient = AdminClient.create(props)) {
                    adminClient.listTopics(new ListTopicsOptions().timeoutMs(5000)).listings().get();
                    return true;
                } catch (InterruptedException | ExecutionException e1) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    if (get())
                        JOptionPane.showMessageDialog(null, "连接成功", "测试连接", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "无法连接", "测试连接", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        }.execute());

        Box testLine = Box.createHorizontalBox();
        testLine.add(testConnLabel);
        testLine.add(Box.createHorizontalStrut(10));
        testLine.add(testConnField);
        testLine.add(Box.createHorizontalStrut(10));
        testLine.add(testConn);

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
                genProps();
                send();
                return null;
            }
        }.execute());
        stop.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            stopFlag.set(true);
            setAbleToStart(true);
        }));
        advanced.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            if (advanced.getText().equals("<<")) {
                advanced.setText(">>");
                scrollPane.setVisible(true);
            } else {
                advanced.setText("<<");
                scrollPane.setVisible(false);
            }
            updateUI();
        }));

        Box controlLine = Box.createHorizontalBox();
        controlLine.add(start);
        controlLine.add(Box.createHorizontalStrut(20));
        controlLine.add(stop);
        controlLine.add(Box.createHorizontalGlue());
        controlLine.add(advanced);

        Box controlPane = Box.createVerticalBox();
        controlPane.setBorder(new EmptyBorder(10, 40, 10, 40));
        controlPane.add(testLine);
        controlPane.add(Box.createVerticalStrut(10));
        controlPane.add(controlLine);
        centerPane.add(controlPane, BorderLayout.SOUTH);
    }

    private void genProps() {
        producerProps.setProperty("bootstrap.servers", testConnField.getText());
        ConfLoader.writeProps("producer.properties", producerProps);

        Properties generatorProps = ConfLoader.loadProps("generator.properties");
        Properties mainProps = mainPane.genProps();
        Properties advancedProps = advancedPane.genProps();
        generatorProps.putAll(mainProps);
        generatorProps.putAll(advancedProps);
        ConfLoader.writeProps("generator.properties", generatorProps);
    }

    private void send() {

    }

    private void setAbleToStart(boolean state) {
        start.setEnabled(state);
        stop.setEnabled(!state);
    }
}

package io.transwarp.streamgui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Author: stk
 * Date: 2018/3/13
 */
public class MainFrame extends JFrame {
    private static JTextArea textArea;
    private JSplitPane splitPane;
    private JPanel panel;

    public MainFrame() {
        double scale_x = 0.5;
        double scale_y = 0.8;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * scale_x);
        int height = (int) (screen.height * scale_y);
        setPreferredSize(new Dimension(width, height));
        setLocation((int) (screen.width * (1 - scale_x) / 2), (int) (screen.height * (1 - scale_y) / 2));
        setTitle("Stream Generator v0.1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();
        initPanel();
        initSplitPane();

        pack();
        setVisible(true);

        splitPane.setDividerLocation(0.5);
    }

    public static void main(String[] args) {
        /*
        Redirect System.out.
         */
        OutputStream textAreaStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getText().length());
            }

            @Override
            public void write(byte[] b) throws IOException {
                textArea.append(new String(b));
                textArea.setCaretPosition(textArea.getText().length());
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                textArea.append(new String(b, off, len));
                textArea.setCaretPosition(textArea.getText().length());
            }
        };
        PrintStream textAreaOut = new PrintStream(textAreaStream);
        System.setOut(textAreaOut);
        System.setErr(textAreaOut);
        new MainFrame();
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("文件");
        JMenu menu2 = new JMenu("编辑");

        JMenuItem item1_1 = new JMenuItem("退出");

        JMenuItem item2_1 = new JMenuItem("连接");
        JMenuItem item2_2 = new JMenuItem("自定义模板");

        menu1.add(item1_1);
        menu2.add(item2_1);
        menu2.add(item2_2);

        menuBar.add(menu1);
        menuBar.add(menu2);

        setJMenuBar(menuBar);
    }

    private void initPanel() {
        panel = new SenderPanel();
    }

    /**
     * Initiate SplitPane and TextArea (used for output).
     */
    private void initSplitPane() {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setLeftComponent(panel);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        splitPane.setRightComponent(scrollPane);

        setContentPane(splitPane);
    }

    private void appendMsg(String msg) {
        textArea.append(msg);
        textArea.append(System.getProperty("line.separator"));
        textArea.setCaretPosition(textArea.getText().length());
    }
}

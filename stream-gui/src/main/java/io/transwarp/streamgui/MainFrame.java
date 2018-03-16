package io.transwarp.streamgui;

import io.transwarp.streamgui.panel.ConsoleOutputStream;
import io.transwarp.streamgui.panel.ControlPanel;
import io.transwarp.streamgui.panel.CustomPanel;
import io.transwarp.streamgui.panel.SenderPanel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.PrintStream;
import java.util.Enumeration;

/**
 * Author: stk
 * Date: 2018/3/13
 * <p>
 * Entrance frame.
 */
public class MainFrame extends JFrame {
    private JSplitPane splitPane;
    private JPanel panel;

    public MainFrame() {
        double scale_x = 0.6;
        double scale_y = 0.8;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * scale_x);
        int height = (int) (screen.height * scale_y);
        setPreferredSize(new Dimension(width, height));
        setLocation((int) (screen.width * (1 - scale_x) / 2), (int) (screen.height * (1 - scale_y) / 2));
        setTitle("Stream Generator v1.0.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();
        panel = new ControlPanel();
        initSplitPane();

        pack();
        setVisible(true);
        splitPane.setDividerLocation(0.6);
    }

    public static void main(String[] args) {
        /*
        Set global font.
         */
        FontUIResource fontUIResource = new FontUIResource(new Font("宋体", Font.PLAIN, 14));
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }
        SwingUtilities.invokeLater(MainFrame::new);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("文件");
        JMenu menu2 = new JMenu("编辑");

        JMenuItem item1_1 = new JMenuItem("退出");
        JMenuItem item2_1 = new JMenuItem("预置模板");
        JMenuItem item2_2 = new JMenuItem("自定义模板");

        menu1.add(item1_1);
        menu2.add(item2_1);
        menu2.add(item2_2);
        menuBar.add(menu1);
        menuBar.add(menu2);
        setJMenuBar(menuBar);

        item1_1.addActionListener(e -> System.exit(0));
        item2_1.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            if (panel != null) remove(panel);
            panel = new SenderPanel();
            splitPane.setLeftComponent(panel);
            revalidate();
        }));
        item2_2.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            if (panel != null) remove(panel);
            panel = new CustomPanel();
            splitPane.setLeftComponent(panel);
            revalidate();
        }));
    }

    /**
     * Initiate SplitPane and TextArea (used for output).
     */
    private void initSplitPane() {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(panel);
        JTextPane textPane = new JTextPane();
        System.setOut(new PrintStream(new ConsoleOutputStream(textPane, Color.BLACK, System.out)));
        System.setErr(new PrintStream(new ConsoleOutputStream(textPane, Color.RED, System.err)));
//        JConsole console = new JConsole(textPane);
//        console.redirectOut(Color.BLACK, System.out);
//        console.redirectErr(Color.RED, System.err);
//        console.setLimitLines(500);
        splitPane.setRightComponent(new JScrollPane(textPane));
        setContentPane(splitPane);
    }
}

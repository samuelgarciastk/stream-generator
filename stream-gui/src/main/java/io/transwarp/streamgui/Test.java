package io.transwarp.streamgui;

import javax.swing.*;
import java.awt.*;

/**
 * Author: stk
 * Date: 2018/3/12
 */
public class Test {
    private JPanel TestPanel;
    private JButton button1;
    private JTextArea textArea1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.5);
        int height = (int) (screen.height * 0.5);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setContentPane(new Test().TestPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

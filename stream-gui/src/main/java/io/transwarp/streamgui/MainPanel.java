package io.transwarp.streamgui;

import javax.swing.*;
import java.awt.*;

/**
 * Author: stk
 * Date: 2018/3/12
 */
public class MainPanel extends JPanel {
    private JTextArea console;

    public MainPanel() {
//        setLayout(null);
//        add(console);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Stream Generator");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.8);
        int height = (int) (screen.height * 0.8);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setContentPane(new MainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

package io.transwarp.streamgui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Author: stk
 * Date: 2018/3/14
 * <p>
 * Console.
 */
public class ConsolePane extends JScrollPane {
    private static ConsolePane consolePane = null;
    private JTextPane textPane;

    private ConsolePane() {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                ConsolePane.this.write(String.valueOf((char) b), Color.BLACK);
            }

            @Override
            public void write(byte[] b) {
                ConsolePane.this.write(new String(b), Color.BLACK);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                ConsolePane.this.write(new String(b, off, len), Color.BLACK);
            }
        }));
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                ConsolePane.this.write(String.valueOf((char) b), Color.RED);
            }

            @Override
            public void write(byte[] b) {
                ConsolePane.this.write(new String(b), Color.RED);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                ConsolePane.this.write(new String(b, off, len), Color.RED);
            }
        }));
        textPane = new JTextPane();
        textPane.setEditable(false);
        setViewportView(textPane);
    }

    public static synchronized ConsolePane getInstance() {
        if (consolePane == null) {
            consolePane = new ConsolePane();
        }
        return consolePane;
    }

    private void write(String msg, Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = doc.addStyle(color.toString(), null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        textPane.setCaretPosition(textPane.getDocument().getLength());
    }

    private final int getLineCount() {
        return textPane.getDocument().getDefaultRootElement().getElementCount();
    }
}

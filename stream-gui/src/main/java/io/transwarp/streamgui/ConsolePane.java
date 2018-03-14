package io.transwarp.streamgui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
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

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Document document = e.getDocument();
                    Element root = document.getDefaultRootElement();
                    while (root.getElementCount() > 1000) {
                        try {
                            document.remove(0, root.getElement(0).getEndOffset());
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

//        int idealLine = 150;
//        int maxExcess = 50;
//
//        int excess = textPane.getDocument().getDefaultRootElement().getElementCount() - idealLine;
//        if (excess >= maxExcess) {
//            replaceRange("", 0, getLineStartOffset(excess));
//        }
    }

//    private int getLineStartOffset(int line) {
//        Element lineElement = textPane.getDocument().getDefaultRootElement().getElement(line);
//        if (lineElement == null)
//            return -1;
//        else
//            return lineElement.getStartOffset();
//    }
//
//    private void replaceRange(String str, int start, int end) {
//        if (end < start) {
//            throw new IllegalArgumentException("end before start");
//        }
//        Document doc = textPane.getDocument();
//        if (doc != null) {
//            try {
//                if (doc instanceof AbstractDocument) {
//                    ((AbstractDocument) doc).replace(start, end - start, str,
//                            null);
//                } else {
//                    doc.remove(start, end - start);
//                    doc.insertString(start, str, null);
//                }
//            } catch (BadLocationException e) {
//                throw new IllegalArgumentException(e.getMessage());
//            }
//        }
//    }
}

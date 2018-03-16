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
 * Date: 2018/3/15
 */
public class JConsole {
    private JTextComponent textComponent;
    private Document document;

    public JConsole(JTextComponent textComponent) {
        this.textComponent = textComponent;
        this.document = textComponent.getDocument();
        textComponent.setEditable(false);
    }

    public void redirectOut(Color textColor, PrintStream printStream) {
        System.setOut(new PrintStream(new ConsoleOutputStream(textColor, printStream), true));
    }

    public void redirectErr(Color textColor, PrintStream printStream) {
        System.setErr(new PrintStream(new ConsoleOutputStream(textColor, printStream), true));
    }

    public void setLimitLines(int lines) {
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Document doc = e.getDocument();
                    Element root = doc.getDefaultRootElement();
                    while (root.getElementCount() > lines) {
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
    }

    class ConsoleOutputStream extends OutputStream {
        private SimpleAttributeSet attributes;
        private PrintStream printStream;

        ConsoleOutputStream(Color textColor, PrintStream printStream) {
            attributes = new SimpleAttributeSet();
            StyleConstants.setForeground(attributes, textColor);
            this.printStream = printStream;
        }

        private synchronized void append(String line) {
            if (printStream != null) printStream.print(line);
            SwingUtilities.invokeLater(() -> {
                try {
                    document.insertString(document.getLength(), line, attributes);
                    textComponent.setCaretPosition(document.getLength());
                } catch (BadLocationException ignored) {
                }
            });
        }

        @Override
        public synchronized void write(int b) {
            append(String.valueOf((char) b));
        }

        @Override
        public synchronized void write(byte[] b) {
            append(new String(b));
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) {
            append(new String(b, off, len));
        }
    }
}

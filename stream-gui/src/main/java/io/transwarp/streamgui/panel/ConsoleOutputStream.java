package io.transwarp.streamgui.panel;

import javax.swing.text.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class ConsoleOutputStream extends OutputStream {
    private Appender appender;

    public ConsoleOutputStream(JTextComponent textComponent, Color color, PrintStream printStream) {
        textComponent.setEditable(false);
        appender = new Appender(textComponent, 500, color, printStream);
    }

    public synchronized void clear() {
        if (appender != null) appender.clear();
    }

    @Override
    public synchronized void write(int b) {
        if (appender != null) appender.append(String.valueOf((char) b));
    }

    @Override
    public synchronized void write(byte[] b) {
        if (appender != null) appender.append(new String(b));
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        if (appender != null) appender.append(new String(b, off, len));
    }

    @Override
    public synchronized void close() {
        appender = null;
    }

    static class Appender implements Runnable {
        private static final String EOL1 = "\n";
        private static final String EOL2 = System.getProperty("line.separator", EOL1);
        private final JTextComponent textComponent;
        private final int limit;
        private final SimpleAttributeSet attributes;
        private final PrintStream printStream;
        private final LinkedList<Integer> lengths;
        private final List<String> values;
        private int currentLength;
        private boolean clear;
        private boolean queue;

        Appender(JTextComponent textComponent, int limit, Color color, PrintStream printStream) {
            this.textComponent = textComponent;
            this.limit = limit;
            attributes = new SimpleAttributeSet();
            StyleConstants.setForeground(attributes, color);
            this.printStream = printStream;
            lengths = new LinkedList<>();
            values = new ArrayList<>();
            currentLength = 0;
            clear = false;
            queue = true;
        }

        synchronized void append(String line) {
            values.add(line);
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        synchronized void clear() {
            clear = true;
            currentLength = 0;
            lengths.clear();
            values.clear();
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        @Override
        public synchronized void run() {
            Document document = textComponent.getDocument();
            if (clear) textComponent.setText("");
            values.forEach(s -> {
                currentLength += s.length();
                if (s.endsWith(EOL1) || s.endsWith(EOL2)) {
                    if (lengths.size() >= limit) {
                        try {
                            document.remove(0, lengths.removeFirst());
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                    lengths.addLast(currentLength);
                    currentLength = 0;
                }
                try {
                    document.insertString(document.getLength(), s, attributes);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                printStream.print(s);
            });
            values.clear();
            clear = false;
            queue = true;
        }
    }
}

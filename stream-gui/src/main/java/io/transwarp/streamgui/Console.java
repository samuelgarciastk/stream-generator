package io.transwarp.streamgui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Author: stk
 * Date: 2018/3/15
 * <p>
 * Create a simple console to display text messages.
 * Messages can be directed here from different sources. Each source can have its messages displayed in a different color.
 * You can limit the number of lines to hold in the Document.
 * Reference: https://tips4java.wordpress.com/2008/11/08/message-console/
 */
public class Console {
    private JTextComponent textComponent;
    private Document document;
    private DocumentListener limitLinesListener;

    /**
     * Use the text component specified as a simply console to display text messages.
     *
     * @param textComponent text component. E.g., JTextArea, JTextPane.
     */
    public Console(JTextComponent textComponent) {
        this.textComponent = textComponent;
        this.document = textComponent.getDocument();
        textComponent.setEditable(false);
    }

    /**
     * Redirect the output from the standard output to the console using the specified color and PrintStream.
     * When a PrintStream is specified the message will be added to the Document before it is also written to the PrintStream.
     *
     * @param textColor   foreground color
     * @param printStream print stream
     */
    public void redirectOut(Color textColor, PrintStream printStream) {
        ConsoleOutputStream cos = new ConsoleOutputStream(textColor, printStream);
        System.setOut(new PrintStream(cos, true));
    }

    /**
     * Redirect the output from the standard error to the console using the specified color and PrintStream.
     * When a PrintStream is specified the message will be added to the Document before it is also written to the PrintStream.
     *
     * @param textColor   foreground color
     * @param printStream print stream
     */
    public void redirectErr(Color textColor, PrintStream printStream) {
        ConsoleOutputStream cos = new ConsoleOutputStream(textColor, printStream);
        System.setErr(new PrintStream(cos, true));
    }

    /**
     * To prevent memory from being used up you can control the number of lines to display in the console.
     * This number can be dynamically changed, but the console will only be updated the next time the Document is updated.
     *
     * @param lines limit number of lines
     */
    public void setMessageLines(int lines) {
        if (limitLinesListener != null) document.removeDocumentListener(limitLinesListener);
        limitLinesListener = new LimitLinesDocumentListener(lines);
        document.addDocumentListener(limitLinesListener);
    }

    /**
     * Class to intercept output from a PrintStream and add it to a Document.
     * The output can optionally be redirected to a different PrintStream.
     * The text displayed in the Document can be color coded to indicate the output source.
     */
    class ConsoleOutputStream extends ByteArrayOutputStream {
        private final String EOL = System.getProperty("line.separator");
        private SimpleAttributeSet attributes;
        private PrintStream printStream;
        private StringBuffer buffer = new StringBuffer(80);
        private boolean isFirstLine;

        ConsoleOutputStream(Color textColor, PrintStream printStream) {
            if (textColor != null) {
                attributes = new SimpleAttributeSet();
                StyleConstants.setForeground(attributes, textColor);
            }
            this.printStream = printStream;
            this.isFirstLine = true;
        }

        /**
         * Override this method to intercept the output text. Each line of text output will actually involve invoking this method twice:
         * a) for the actual text message
         * b) for the newLine string
         */
        public void flush() {
            String message = toString();
            if (message.length() == 0) return;
            if (document.getLength() == 0) buffer.setLength(0);
            buffer.append(message);
            if (!message.equals(EOL)) {
                clearBuffer();
            }
            reset();
        }

        /**
         * The message and the newLine have been added to the buffer in the appropriate order so we can now update the Document and send the text to the optional PrintStream.
         */
        private void clearBuffer() {
            /*
            In case both the standard out and standard err are being redirected,
            we need to insert a newline character for the first line only.
             */
            if (isFirstLine && document.getLength() != 0) buffer.insert(0, "\n");
            isFirstLine = false;
            String line = buffer.toString();
            try {
                document.insertString(document.getLength(), line, attributes);
                textComponent.setCaretPosition(document.getLength());
            } catch (BadLocationException ignored) {
            }
            if (printStream != null) printStream.print(line);
            buffer.setLength(0);
        }
    }

    /**
     * Document listener for controlling the number of lines to display in the console.
     */
    class LimitLinesDocumentListener implements DocumentListener {
        private int maximumLines;

        LimitLinesDocumentListener(int maximumLines) {
            this.maximumLines = maximumLines;
        }

        public void insertUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(() -> {
                Document document = e.getDocument();
                Element root = document.getDefaultRootElement();
                while (root.getElementCount() > maximumLines) {
                    try {
                        document.remove(0, root.getElement(0).getEndOffset());
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        public void removeUpdate(DocumentEvent e) {
        }

        public void changedUpdate(DocumentEvent e) {
        }
    }
}

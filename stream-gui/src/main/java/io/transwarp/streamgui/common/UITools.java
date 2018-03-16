package io.transwarp.streamgui.common;

import javax.swing.*;
import java.awt.*;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public class UITools {
    public static JComponent setFixedSize(JComponent component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        return component;
    }
}

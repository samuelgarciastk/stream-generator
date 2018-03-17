package io.transwarp.streamgui.panel;

import javax.swing.*;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public abstract class PropsBox extends Box {
    PropsBox(int axis) {
        super(axis);
    }

    public abstract Properties genProps();
}

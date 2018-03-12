package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/7
 * <p>
 * Generate random e-mail.
 * There are ten characters before '@' in default.
 * Configuration files: email_suffix
 */
public class Email implements DataGen {
    private static final List<String> suffix = ConfLoader.loadConf("email_suffix");

    @Override
    public String nextRecord() {
        return StringGenerator.randomString(10, 0, 36) +
                suffix.get((int) (Math.random() * suffix.size()));
    }
}

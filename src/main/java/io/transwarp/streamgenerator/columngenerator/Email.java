package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/7
 */
public class Email implements DataGen {
    private static final List<String> suffix = ConfLoader.loadConf("email_suffix");

    @Override
    public String nextRecord() {
        return StringGenerator.randomString(10, 0, 36) +
                suffix.get((int) (Math.random() * suffix.size()));
    }
}

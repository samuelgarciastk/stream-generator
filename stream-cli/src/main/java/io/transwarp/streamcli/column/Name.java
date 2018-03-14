package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;

import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random Chinese name. The default length of given name is two.
 * Configuration files: first_name, chinese_words
 */
public class Name implements DataGen {
    private List<String> firstName;
    private String base;

    public Name(Properties props) {
        firstName = ConfLoader.loadConf("first_name");
        base = ConfLoader.loadString("chinese_words");
    }

    @Override
    public String nextRecord() {
        return firstName.get((int) (Math.random() * firstName.size())) +
                StringGenerator.randomString(2, base);
    }
}

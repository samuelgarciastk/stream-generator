package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class Name implements DataGen {
    private static final List<String> firstName = ConfLoader.loadConf("first_name");
    private static final String base = ConfLoader.loadString("chinese_words");

    @Override
    public String nextRecord() {
        return firstName.get((int) (Math.random() * firstName.size())) +
                StringGenerator.randomString(2, base);
    }
}

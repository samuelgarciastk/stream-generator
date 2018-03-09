package io.transwarp.streamgenerator.column;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random Chinese phone number.
 * The default length is 12.
 * Configuration files: phone_prefix
 */
public class PhoneNumber implements DataGen {
    private static final List<String> prefix = ConfLoader.loadConf("phone_prefix");

    @Override
    public String nextRecord() {
        return prefix.get((int) (Math.random() * prefix.size())) +
                StringGenerator.randomNumber(9);
    }
}

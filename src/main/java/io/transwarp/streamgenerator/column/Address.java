package io.transwarp.streamgenerator.column;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/1
 * <p>
 * Generate random address information.
 * E.g., 江苏省常州市武进市YXT路637号
 * Configuration files: address_code
 */
public class Address implements DataGen {
    private static final List<String> addresses = ConfLoader.loadConf("address_code", 2, 1);

    @Override
    public String nextRecord() {
        return addresses.get((int) (Math.random() * addresses.size())) +
                StringGenerator.randomUpper(3) + "路" + (int) (Math.random() * 999 + 1) + "号";
    }
}

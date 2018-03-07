package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class Address implements DataGen {
    private static final List<String> addresses = ConfLoader.loadConf("address_code", 2, 1);

    @Override
    public String nextRecord() {
        return addresses.get((int) (Math.random() * addresses.size())) +
                StringGenerator.randomUpper(3) + "路" + (int) (Math.random() * 1000) + "号";
    }
}

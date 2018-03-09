package io.transwarp.streamgenerator.column;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random bank name.
 * Configuration files: bank
 */
public class Bank implements DataGen {
    private static final List<String> bankName = ConfLoader.loadConf("bank");

    @Override
    public String nextRecord() {
        return bankName.get((int) (Math.random() * bankName.size()));
    }
}

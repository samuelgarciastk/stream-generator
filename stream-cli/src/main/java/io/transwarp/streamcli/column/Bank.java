package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

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

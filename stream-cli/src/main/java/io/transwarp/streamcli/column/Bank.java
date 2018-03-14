package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random bank name.
 * Configuration files: bank
 */
public class Bank implements DataGen {
    private List<String> bankName;

    public Bank(Properties props) {
        bankName = ConfLoader.loadConf("bank");
    }

    @Override
    public String nextRecord() {
        return bankName.get((int) (Math.random() * bankName.size()));
    }
}

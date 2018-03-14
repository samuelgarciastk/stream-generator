package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/5
 * <p>
 * Generate the random code of exchange office.
 * Configuration files: safe_info
 */
public class SafeInfo implements DataGen {
    private List<String> safeCode;

    public SafeInfo(Properties props) {
        safeCode = ConfLoader.loadConf("safe_info", 2, 1);
    }

    @Override
    public String nextRecord() {
        return safeCode.get((int) (Math.random() * safeCode.size()));
    }
}

package io.transwarp.streamgenerator.column;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/5
 * <p>
 * Generate the random code of exchange office.
 * Configuration files: safe_info
 */
public class SafeInfo implements DataGen {
    private static final List<String> safeCode = ConfLoader.loadConf("safe_info", 2, 1);

    @Override
    public String nextRecord() {
        return safeCode.get((int) (Math.random() * safeCode.size()));
    }
}

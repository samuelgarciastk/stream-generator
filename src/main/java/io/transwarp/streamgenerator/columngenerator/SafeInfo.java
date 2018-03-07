package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/5
 */
public class SafeInfo implements DataGen {
    private static final List<String> safeCode = ConfLoader.loadConf("safe_info", 2, 1);

    @Override
    public String nextRecord() {
        return safeCode.get((int) (Math.random() * safeCode.size()));
    }
}

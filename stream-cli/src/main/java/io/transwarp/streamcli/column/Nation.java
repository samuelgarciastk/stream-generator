package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random nation.
 * Configuration files: nation
 */
public class Nation implements DataGen {
    private static final List<String> nation = ConfLoader.loadConf("nation");

    @Override
    public String nextRecord() {
        return nation.get((int) (Math.random() * nation.size()));
    }
}

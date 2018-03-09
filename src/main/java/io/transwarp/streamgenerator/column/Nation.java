package io.transwarp.streamgenerator.column;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.ConfLoader;

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

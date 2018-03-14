package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random nation.
 * Configuration files: nation
 */
public class Nation implements DataGen {
    private List<String> nation;

    public Nation(Properties props) {
        nation = ConfLoader.loadConf("nation");
    }

    @Override
    public String nextRecord() {
        return nation.get((int) (Math.random() * nation.size()));
    }
}

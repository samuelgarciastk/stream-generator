package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.ConfLoader;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/12
 * <p>
 * Generate random city in China.
 * Configuration files: cities
 */
public class City implements DataGen {
    private static final List<String> cities = ConfLoader.loadConf("cities");

    @Override
    public String nextRecord() {
        return cities.get((int) (Math.random() * cities.size()));
    }
}

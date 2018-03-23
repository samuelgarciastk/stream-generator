package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/12
 * <p>
 * Generate random city in China.
 * Configuration files: cities
 */
public class City implements DataGen {
    private List<String> cities;

    public City(List<String> configs) {
        cities = ConfLoader.loadConf("cities");
    }

    @Override
    public String nextRecord() {
        return cities.get((int) (Math.random() * cities.size()));
    }
}

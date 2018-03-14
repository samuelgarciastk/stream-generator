package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;

import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/12
 * <p>
 * Generate random city in China.
 * Configuration files: cities
 */
public class City implements DataGen {
    private List<String> cities;

    public City(Properties props) {
        cities = ConfLoader.loadConf("cities");
    }

    @Override
    public String nextRecord() {
        return cities.get((int) (Math.random() * cities.size()));
    }
}

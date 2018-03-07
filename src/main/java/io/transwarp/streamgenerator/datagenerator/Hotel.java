package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.columngenerator.Address;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class Hotel implements DataGen {
    private Address address = new Address();
    private String delimiter;

    public Hotel(Properties props) {
        delimiter = props.getProperty("delimiter");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        result.add(StringGenerator.randomString((int) (Math.random() * 4 + 6)) + "酒店");
        result.add(address.nextRecord());
        return result.toString();
    }
}

package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.column.Address;
import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;

import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/1
 * <p>
 * Generate random hotel information.
 * Format: 酒店名称, 地址
 * E.g., J5DYVQ60酒店,辽宁省辽阳市弓长岭区MUH路545号
 */
public class Hotel implements DataGen {
    private Address address;
    private String delimiter;

    public Hotel(Properties props) {
        address = new Address(props);
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

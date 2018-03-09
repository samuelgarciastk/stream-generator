package io.transwarp.streamgenerator.schema;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.column.Address;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class Hotel implements DataGen {
    private Address address = new Address();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        result.add(StringGenerator.randomString((int) (Math.random() * 4 + 6)) + "酒店");
        result.add(address.nextRecord());
        return result.toString();
    }
}

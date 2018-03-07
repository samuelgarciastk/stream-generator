package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.columngenerator.Address;
import io.transwarp.streamgenerator.columngenerator.Gender;
import io.transwarp.streamgenerator.columngenerator.Name;
import io.transwarp.streamgenerator.columngenerator.Nation;

import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class People implements DataGen {
    private Name name = new Name();
    private Gender gender = new Gender();
    private Nation nation = new Nation();
    private Address address = new Address();
    private String delimiter;

    public People(Properties props) {
        String beginDate = props.getProperty("people.begin.date");
        String endDate = props.getProperty("people.end.date");
        delimiter = props.getProperty("delimiter");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        result.add(name.nextRecord());
        result.add(gender.nextRecord());
        result.add(nation.nextRecord());
        result.add(address.nextRecord());
        return result.toString();
    }
}

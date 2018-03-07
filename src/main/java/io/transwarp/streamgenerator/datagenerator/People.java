package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.columngenerator.Address;
import io.transwarp.streamgenerator.columngenerator.Name;
import io.transwarp.streamgenerator.columngenerator.Nation;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class People implements DataGen {
    private Name name = new Name();
    private Nation nation = new Nation();
    private Address address = new Address();

    public People(Properties props) {
        String beginDate = props.getProperty("people.begin.date");
        String endDate = props.getProperty("people.end.date");
    }

    @Override
    public String nextRecord(int colNum) {
        List<String> result = new ArrayList<>();
        result.add(name.nextRecord(0));
        result.add(Gender);
        result.add(nation.nextRecord(0));
        result.add(address.nextRecord(0));
        return result.stream().limit(colNum).collect(Collectors.joining(","));
    }
}

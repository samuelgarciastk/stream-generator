package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.columngenerator.Address;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hotel implements DataGen {
    private Address address = new Address();

    @Override
    public String nextRecord() {
        List<String> result = new ArrayList<>();
        result.add(StringGenerator.randomString((int) (Math.random() * 4 + 6)) + "酒店");
        result.add(address.nextRecord());
        return result.stream().collect(Collectors.joining(","));
    }
}

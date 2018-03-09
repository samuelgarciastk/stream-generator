package io.transwarp.streamgenerator.schema;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.column.*;

import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class People implements DataGen {
    private Name name = new Name();
    private Nation nation = new Nation();
    private IDCard idCard = new IDCard();
    private PhoneNumber phoneNumber = new PhoneNumber();
    private Email email = new Email();
    private Address address = new Address();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String id = idCard.nextRecord();
        result.add(name.nextRecord());
        result.add(Integer.parseInt(String.valueOf(id.charAt(16))) % 2 == 1 ? "男" : "女");
        result.add(nation.nextRecord());
        result.add(id);
        result.add(id.substring(6, 14));
        result.add(phoneNumber.nextRecord());
        result.add(email.nextRecord());
        result.add(address.nextRecord());
        return result.toString();
    }
}

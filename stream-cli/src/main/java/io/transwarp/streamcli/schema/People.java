package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.column.*;
import io.transwarp.streamcli.common.DataGen;

import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random people information.
 * Format: 姓名, 性别, 民族, 身份证号, 出生日期, 手机号, 邮箱地址, 家庭住址
 * E.g., 练兴琼,男,汉族,33048219500103898X,19500103,158386410295,4rse66agc0@gmail.com,贵州省六盘水市钟山区WRP路128号
 */
public class People implements DataGen {
    private Name name;
    private Nation nation;
    private IDCard idCard;
    private PhoneNumber phoneNumber;
    private Email email;
    private Address address;
    private String delimiter;

    public People(Properties props) {
        name = new Name(props);
        nation = new Nation(props);
        idCard = new IDCard(props);
        phoneNumber = new PhoneNumber(props);
        email = new Email(props);
        address = new Address(props);
        delimiter = props.getProperty("delimiter");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
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

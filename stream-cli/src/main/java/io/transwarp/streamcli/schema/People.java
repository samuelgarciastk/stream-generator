package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.column.*;

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

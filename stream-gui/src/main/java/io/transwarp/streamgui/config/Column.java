package io.transwarp.streamgui.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public enum Column {
    Address("地址"),
    Bank("银行名称"),
    Email("邮箱"),
    Gender("性别"),
    IDCard("身份证号"),
    Name("姓名"),
    Nation("民族"),
    PhoneNumber("手机号码"),
    SafeInfo("外汇局代码");
    private static Map<String, Column> map;

    static {
        map = new HashMap<>();
        Arrays.stream(Column.values()).forEach(i -> map.put(i.getName(), i));
    }

    private String name;

    Column(String name) {
        this.name = name;
    }

    public static Column getEnum(String name) {
        return map.get(name);
    }

    public String getName() {
        return name;
    }
}

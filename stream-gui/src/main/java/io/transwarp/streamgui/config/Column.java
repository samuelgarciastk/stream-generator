package io.transwarp.streamgui.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public enum Column {
    Address("地址", null),
    Bank("银行名称", null),
    City("城市名称", null),
    Email("邮箱", null),
    Gender("性别", null),
    IDCard("身份证号", List.of("开始日期", "结束日期")),
    Name("姓名", null),
    Nation("民族", null),
    PhoneNumber("手机号码", null),
    RegexString("正则字符串", List.of("正则表达式")),
    SafeInfo("外汇局代码", null),
    Timestamp("时间", List.of("格式", "开始时间", "结束时间"));
    private static Map<String, Column> map;

    static {
        map = new HashMap<>();
        Arrays.stream(Column.values()).forEach(i -> map.put(i.getName(), i));
    }

    private String name;
    private List<String> configs;

    Column(String name, List<String> configs) {
        this.name = name;
        this.configs = configs;
    }

    public static Column getEnum(String name) {
        return map.get(name);
    }

    public String getName() {
        return name;
    }

    public List<String> getConfigs() {
        return configs;
    }
}

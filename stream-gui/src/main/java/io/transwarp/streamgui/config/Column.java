package io.transwarp.streamgui.config;

import java.util.*;

/**
 * Author: stk
 * Date: 2018/3/16
 */
public enum Column {
    Address("地址", null, null),
    Bank("银行名称", null, null),
    City("城市名称", null, null),
    Email("邮箱", null, null),
    Gender("性别", null, null),
    IDCard("身份证号", Arrays.asList("开始日期(yyyyMMdd)", "结束日期(yyyyMMdd)"), Arrays.asList("19500101", "20101231")),
    Name("姓名", null, null),
    Nation("民族", null, null),
    PhoneNumber("手机号码", null, null),
    RegexString("正则字符串", Collections.singletonList("正则表达式"), Collections.singletonList("[a-zA-Z0-9]{5,10}")),
    SafeInfo("外汇局代码", null, null),
    Timestamp("时间", Arrays.asList("格式", "开始时间", "结束时间"), Arrays.asList("yyyy/MM/dd HH:mm:ss", "1980/01/01 00:00:00", "2015/12/31 23:59:59"));
    private static Map<String, Column> map;

    static {
        map = new HashMap<>();
        Arrays.stream(Column.values()).forEach(i -> map.put(i.getName(), i));
    }

    private String name;
    private List<String> configs;
    private List<String> defaultConfigs;

    Column(String name, List<String> configs, List<String> defaultConfigs) {
        this.name = name;
        this.configs = configs;
        this.defaultConfigs = defaultConfigs;
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

    public List<String> getDefaultConfigs() {
        return defaultConfigs;
    }
}

package io.transwarp.streamgui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: stk
 * Date: 2018/3/14
 * <p>
 * Store detailed information of each template.
 */
public enum Template {
    BankAccount("银行账号",
            "开户行, 卡号, 开户日期, 开户人身份证, 开户人姓名, 开户名手机号",
            Arrays.asList("bank.account.base", "bank.account.date.end", "bank.account.id.length")),
    BankBill("银行账单明细",
            "转出账号, 转账人姓名, 收款账号, 收款人姓名, 转账金额, 交易类型, 转账渠道, 转账方式, 转账时间, 转账附言, 手续费",
            Arrays.asList("bank.bill.base", "bank.bill.charge", "bank.bill.date.end", "bank.bill.date.format")),
    Bus("汽车出行",
            "姓名, 性别, 身份证号, 出行日期, 车次, 出发汽车站, 发车时间, 座位号, 出发城市, 到达城市",
            Arrays.asList("travel.date.end", "travel.date.format")),
    CDC("CDC",
            "\"2016-06-12 11:02:03\",\"1465700523694\",\"U\",\"CDC_MIRROR\",\"7325830911134034944\",\"ZQU\",\"6217844638089027584\",\"MU\"",
            null),
    Flight("飞机出行",
            "姓名, 英文名, 身份证号, 性别, 航班号, 出行日期, 座位号, 起飞时间, 起飞城市, 到达城市",
            Arrays.asList("travel.date.end", "travel.date.format")),
    Hotel("酒店",
            "酒店名称, 地址",
            null),
    People("人口",
            "姓名, 性别, 民族, 身份证号, 出生日期, 手机号, 邮箱地址, 家庭住址",
            Arrays.asList("people.birthday.begin", "people.birthday.end")),
    PhoneCall("话单数据",
            "主叫人姓名, 主叫人身份证, 主叫人电话号码, 被叫人姓名, 被叫人身份证, 被叫人电话号码, 通话日期, 开始时间, 通话时常",
            Arrays.asList("phone.date.end", "phone.date.format", "phone.duration.base")),
    S_CORP_RCV("外汇局RCV数据",
            "S_CORP_RCV;RPTNO, 企业组织机构代码, RCV_DATE, SAFECODE, 收汇美元金额, 交易代码",
            Arrays.asList("safe.base", "safe.date.begin.year", "safe.date.end.year", "safe.date.format", "safe.prefix")),
    S_EXPF("外汇局EXPF数据",
            "S_EXPF;CUSTOM_RPT_NO, 企业组织机构代码, EXP_DATE",
            Arrays.asList("safe.base", "safe.date.begin.year", "safe.date.end.year", "safe.date.format", "safe.prefix")),
    Train("火车出行",
            "姓名, 性别, 身份证号, 车次, 出行日期, 车厢号, 座位号, 出发城市, 到达城市, 出发时间",
            Arrays.asList("travel.date.end", "travel.date.format"));
    private static Map<String, Template> map;

    static {
        map = new HashMap<>();
        Arrays.stream(Template.values()).forEach(i -> map.put(i.getName(), i));
    }

    private String name;
    private String desc;
    private List<String> config;

    Template(String name, String desc, List<String> config) {
        this.name = name;
        this.desc = desc;
        this.config = config;
    }

    static Template getEnum(String name) {
        return map.get(name);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getConfig() {
        return config;
    }
}

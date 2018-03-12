package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/9
 * <p>
 * Generate random flight travel information.
 * Format: 姓名, 英文名, 身份证号, 性别, 航班号, 出行日期, 座位号, 起飞时间, 起飞城市, 到达城市
 * E.g., 饶佳瑞,MxvMTfWcNOmrh,230802195001016154,女,MF10,19950803,52G,0:30,珠海,贵阳
 * Configuration files; flight
 */
public class Flight implements DataGen {
    private static final List<String> timeTable = ConfLoader.loadData("flight");
    private People people = new People();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String[] peopleArr = people.nextRecord().split(Generator.delimiter);
        String[] flight = timeTable.get((int) (Math.random() * timeTable.size())).split(",");
        result.add(peopleArr[0]);
        result.add(StringGenerator.randomUpper((int) (Math.random() * 10 + 4)));
        result.add(peopleArr[1]);
        result.add(peopleArr[3]);
        result.add(flight[2]);
        result.add(TimeGenerator.randomDateWithTrans(peopleArr[4],
                Generator.props.getProperty("travel.date.end"),
                Generator.props.getProperty("travel.date.format")));
        result.add((int) (Math.random() * 60 + 1) + StringGenerator.randomString(1, 36, 43));
        result.add(flight[3]);
        result.add(flight[0]);
        result.add(flight[1]);
        return result.toString();
    }
}

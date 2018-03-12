package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random bus travel information.
 * Format: 姓名, 性别, 身份证号, 出行日期, 车次, 出发汽车站, 发车时间, 座位号, 出发城市, 到达城市
 * E.g., 郭东可,女,542528195001016113,20130221,2,天津汽车站,1:30,18,天津,临沂
 * Configuration files: bus
 */
public class Bus implements DataGen {
    private static final List<String> timeTable = ConfLoader.loadData("bus");
    private People people = new People();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String[] peopleArr = people.nextRecord().split(Generator.delimiter);
        String[] bus = timeTable.get((int) (Math.random() * timeTable.size())).split(",");
        result.add(peopleArr[0]);
        result.add(peopleArr[1]);
        result.add(peopleArr[3]);
        result.add(TimeGenerator.randomDateWithTrans(peopleArr[4],
                Generator.props.getProperty("travel.date.end"),
                Generator.props.getProperty("travel.date.format")));
        result.add(String.valueOf((int) (Math.random() * 5 + 1)));
        result.add(bus[0]);
        result.add(bus[1]);
        result.add(String.valueOf((int) (Math.random() * 60 + 1)));
        result.add(bus[2]);
        result.add(bus[3]);
        return result.toString();
    }
}

package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.List;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/9
 * <p>
 * Generate random train travel information.
 * Format: 姓名, 性别, 身份证号, 车次, 出行日期, 车厢号, 座位号, 出发城市, 到达城市, 出发时间
 * E.g., 孔冠鲲,女,152921195001016417,D161,19960304,3,34,许昌,平顶山,13:30
 * Configuration files: train
 */
public class Train implements DataGen {
    private static final List<String> timeTable = ConfLoader.loadData("train");
    private People people = new People();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String[] peopleArr = people.nextRecord().split(Generator.delimiter);
        String[] train = timeTable.get((int) (Math.random() * timeTable.size())).split(",");
        result.add(peopleArr[0]);
        result.add(peopleArr[1]);
        result.add(peopleArr[3]);
        result.add(train[0]);
        result.add(TimeGenerator.randomDateWithTrans(peopleArr[4],
                Generator.props.getProperty("travel.date.end"),
                Generator.props.getProperty("travel.date.format")));
        result.add(String.valueOf((int) (Math.random() * 16 + 1)));
        result.add(String.valueOf((int) (Math.random() * 40 + 1)));
        result.add(train[1]);
        result.add(train[2]);
        result.add(train[3]);
        return result.toString();
    }
}

package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random phone call information.
 * Format: 主叫人姓名, 主叫人身份证, 主叫人电话号码, 被叫人姓名, 被叫人身份证, 被叫人电话号码, 通话日期, 开始时间, 通话时常
 * E.g., 李航铠,659000199402082468,130321216077,樊赐储,659001195604210745,134121316317,20150728,21:53:14,26
 */
public class PhoneCall implements DataGen {
    private final int durationBase = Integer.parseInt(Generator.props.getProperty("phone.duration.base"));
    private People people = new People();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String[] people1 = people.nextRecord().split(Generator.delimiter);
        String[] people2 = people.nextRecord().split(Generator.delimiter);
        while (people1[0].equals(people2[0])) people2 = people.nextRecord().split(Generator.delimiter);
        result.add(people1[0]);
        result.add(people1[3]);
        result.add(people1[5]);
        result.add(people2[0]);
        result.add(people2[3]);
        result.add(people2[5]);
        result.add(TimeGenerator.randomDateWithTrans(people1[4].compareTo(people2[4]) > 0 ? people1[4] : people2[4],
                Generator.props.getProperty("phone.date.end"),
                Generator.props.getProperty("phone.date.format")));
        result.add(TimeGenerator.randomTime());
        result.add(String.valueOf((int) (Math.random() * durationBase) + 1));
        return result.toString();
    }
}

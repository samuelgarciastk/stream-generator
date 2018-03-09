package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/7
 */
public class IDCard implements DataGen {
    private static final List<String> addressCode = ConfLoader.loadConf("address_code", 2, 0);
    private static final String beginDate = Generator.props.getProperty("people.birthday.begin");
    private static final String endDate = Generator.props.getProperty("people.birthday.end");

    private char checkCode(String id) {
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] code = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int result = 0;
        int[] strArr = Arrays.stream(id.split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < strArr.length; i++) result += strArr[i] * weight[i];
        return code[result % 11];
    }

    @Override
    public String nextRecord() {
        String former = addressCode.get((int) (Math.random() * addressCode.size())) +
                TimeGenerator.randomDate(beginDate, endDate, "yyyyMMdd") +
                StringGenerator.randomNumber(3);
        return former + checkCode(former);
    }
}

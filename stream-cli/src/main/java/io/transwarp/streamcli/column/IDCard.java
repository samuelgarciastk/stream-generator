package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.ConfLoader;
import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/7
 * <p>
 * Generate random Chinese ID number.
 * Configuration files: address_code
 */
public class IDCard implements DataGen {
    private List<String> addressCode;
    private String beginDate;
    private String endDate;

    public IDCard(List<String> configs) {
        addressCode = ConfLoader.loadConf("address_code", 2, 0);
        beginDate = configs.get(0);
        endDate = configs.get(1);
    }

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

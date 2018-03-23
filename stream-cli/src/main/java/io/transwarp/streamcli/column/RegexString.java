package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random String from a given regular expression.
 */
public class RegexString implements DataGen {
    private String regex;

    public RegexString(List<String> configs) {
        this.regex = configs.get(0);
    }

    @Override
    public String nextRecord() {
        return StringGenerator.randomRegexString(regex);
    }
}

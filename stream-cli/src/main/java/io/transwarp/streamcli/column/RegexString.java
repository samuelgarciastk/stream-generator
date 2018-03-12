package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.DataGen;

/**
 * Author: stk
 * Date: 2018/3/8
 * <p>
 * Generate random String from a given regular expression.
 */
public class RegexString implements DataGen {
    private String regex;

    public RegexString(String regex) {
        this.regex = regex;
    }

    @Override
    public String nextRecord() {
        return StringGenerator.randomRegexString(regex);
    }
}

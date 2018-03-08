package io.transwarp.streamgenerator.columngenerator;

import com.mifmif.common.regex.Generex;
import io.transwarp.streamgenerator.DataGen;

/**
 * Author: stk
 * Date: 2018/3/8
 */
public class RegexString implements DataGen {
    private String regex;

    public RegexString(String regex) {
        this.regex = regex;
    }

    @Override
    public String nextRecord() {
        return new Generex(regex).random();
    }
}

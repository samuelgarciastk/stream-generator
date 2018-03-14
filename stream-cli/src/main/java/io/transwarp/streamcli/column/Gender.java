package io.transwarp.streamcli.column;

import io.transwarp.streamcli.common.DataGen;

import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random gender.
 */
public class Gender implements DataGen {
    public Gender(Properties props) {
    }

    @Override
    public String nextRecord() {
        return GenderType.getRandom().toString();
    }

    private enum GenderType {
        MAN("男"), WOMAN("女");
        private String value;

        GenderType(String value) {
            this.value = value;
        }

        static GenderType getRandom() {
            return GenderType.values()[(int) (Math.random() * GenderType.values().length)];
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

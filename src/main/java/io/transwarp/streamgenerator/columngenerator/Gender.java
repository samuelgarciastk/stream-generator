package io.transwarp.streamgenerator.columngenerator;

import io.transwarp.streamgenerator.DataGen;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class Gender implements DataGen {
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

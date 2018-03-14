package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.common.StringGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/2
 * <p>
 * Generate random CDC information.
 * E.g., "2016-06-12 11:02:03","1465700523694","U","CDC_MIRROR","7325830911134034944","ZQU","6217844638089027584","MU"
 */
public class CDC implements DataGen {
    private String delimiter;
    private ChangeType lastChangeType;
    private long baseTime = System.currentTimeMillis();

    public CDC(Properties props) {
        delimiter = props.getProperty("delimiter");
    }

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(delimiter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long time = getNextTime();
        String timeStr = sdf.format(new Date(time));
        result.add("\"" + timeStr + "\"");
        result.add("\"" + time + "\"");
        ChangeType changeType = (lastChangeType == ChangeType.B) ? ChangeType.A : ChangeType.getRandom();
        setLastChangeType(changeType);
        result.add("\"" + changeType.toString() + "\"");
        RecordType recordType = RecordType.getRandom();
        result.add("\"" + recordType.toString() + "\"");
        result.add(genRecord(changeType));
        return result.toString();
    }

    private String genRecord(ChangeType changeType) {
        String record1 = "\"" + (long) (Math.random() * Long.MAX_VALUE) + "\",\"" + StringGenerator.randomUpper((int) (Math.random() * 10)) + "\"";
        String result;
        switch (changeType) {
            case I: {
                result = "," + record1;
                break;
            }
            case D: {
                result = record1 + ",";
                break;
            }
            case U: {
                String record2 = "\"" + (long) (Math.random() * Long.MAX_VALUE) + "\",\"" + StringGenerator.randomUpper((int) (Math.random() * 10)) + "\"";
                result = record1 + "," + record2;
                break;
            }
            default: {
                result = record1;
            }
        }
        return result;
    }

    private long getNextTime() {
        return (Math.random() < 0.5) ? System.currentTimeMillis() : baseTime;
    }

    private synchronized void setLastChangeType(ChangeType changeType) {
        lastChangeType = changeType;
    }

    private enum ChangeType {
        I, U, B, A, D;

        static ChangeType getRandom() {
            return ChangeType.values()[(int) (Math.random() * ChangeType.values().length)];
        }
    }

    private enum RecordType {
        CDC_MIRROR;

        static RecordType getRandom() {
            return RecordType.values()[(int) (Math.random() * RecordType.values().length)];
        }
    }
}

package io.transwarp.streamgenerator.datagenerator;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.common.StringGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CDC implements DataGen {
    private static final long baseTime = System.currentTimeMillis();
    private static ChangeType lastChangeType;

    @Override
    public String nextRecord(int colNum) {
        List<String> result = new ArrayList<>();
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
        return result.stream().limit(colNum).collect(Collectors.joining(","));
    }

    private String genRecord(ChangeType changeType) {
        String record1 = "\"" + (long) (Math.random() * Long.MAX_VALUE) + "\",\"" + StringGenerator.randomString((int) (Math.random() * 10), 10, 36) + "\"";
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
                String record2 = "\"" + (long) (Math.random() * Long.MAX_VALUE) + "\",\"" + StringGenerator.randomString((int) (Math.random() * 10), 10, 36) + "\"";
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

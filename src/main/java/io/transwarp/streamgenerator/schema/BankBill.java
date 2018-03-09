package io.transwarp.streamgenerator.schema;

import io.transwarp.streamgenerator.DataGen;
import io.transwarp.streamgenerator.Generator;
import io.transwarp.streamgenerator.common.ConfLoader;
import io.transwarp.streamgenerator.common.StringGenerator;
import io.transwarp.streamgenerator.common.TimeGenerator;

import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 */
public class BankBill implements DataGen {
    private int windowSize = Integer.parseInt(Generator.props.getProperty("bank.bill.window.size"));
    private double ratio = Double.parseDouble(Generator.props.getProperty("bank.bill.ratio"));
    private int billBase = Integer.parseInt(Generator.props.getProperty("bank.bill.base"));
    private double charge = Double.parseDouble(Generator.props.getProperty("bank.bill.charge"));
    private Consumer consumer;

    public BankBill() {
        Properties consumerProps = ConfLoader.loadProps("consumer.properties");
        consumerProps.put("max.poll.records", windowSize);
        consumerProps.put("group.id", "BankBill");
        consumer = new Consumer(Generator.props.getProperty("bank.account.topic"), consumerProps);
    }

    @Override
    public String nextRecord() {
        List<String> bankAccount = consumer.getValues(windowSize);
        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        for (int i = 0; i < windowSize * ratio; i++) {
            StringJoiner line = new StringJoiner(Generator.delimiter);
            String[] account1 = bankAccount.get((int) (Math.random() * bankAccount.size())).split(",");
            String[] account2 = bankAccount.get((int) (Math.random() * bankAccount.size())).split(",");

            String bankName1 = account1[0];
            String cardNum1 = account1[1];
            String date1 = account1[2];
            String people1 = account1[4];

            String bankName2 = account2[0];
            String cardNum2 = account2[1];
            String date2 = account2[2];
            String people2 = account2[4];

            String outCardNum = "";
            String outPeople = "";
            String transactionType;
            if (cardNum1.equalsIgnoreCase(cardNum2)) {
                transactionType = TransactionType.CUN_KUAN.toString();
            } else {
                outCardNum = cardNum1;
                outPeople = people1;
                transactionType = TransactionType.getRandom().toString();
            }
            line.add(outCardNum);
            line.add(outPeople);
            line.add(cardNum2);
            line.add(people2);
            double bill = Math.random() * billBase + 1;
            line.add(String.format("%.2f", bill));
            line.add(transactionType);

            String transactionMode = "";
            String transferChannel = "";
            String transferMode = "";
            if (transactionType.equals(TransactionType.ZHUAN_ZHANG.toString())) {
                transactionMode = bankName1.equalsIgnoreCase(bankName2) ? TransactionMode.TONG_HANG.toString() : TransactionMode.KUA_HANG.toString();
                transferChannel = TransferChannel.getRandom().toString();
                transferMode = TransferMode.getRandom().toString();
            } else if (transactionType.equals(TransactionType.XIAO_FEI.toString())) {
                transactionMode = TransactionMode.POS.toString();
            }
            line.add(transactionMode);
            line.add(transferChannel);
            line.add(transferMode);
            line.add(TimeGenerator.randomDateWithTrans(date1.compareTo(date2) > 0 ? date1 : date2,
                    Generator.props.getProperty("bank.bill.date.end"),
                    Generator.props.getProperty("bank.bill.date.format")));
            line.add(StringGenerator.randomUpper(10));
            line.add(String.format("%.2f", bill * charge));
            result.add(line.toString());
        }
        return result.toString();
    }

    private enum TransactionType {
        ZHUAN_ZHANG("转账"), XIAO_FEI("消费"), CUN_KUAN("存款");
        private String value;

        TransactionType(String value) {
            this.value = value;
        }

        static TransactionType getRandom() {
            return Math.random() < 0.5 ? TransactionType.XIAO_FEI : TransactionType.ZHUAN_ZHANG;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private enum TransferChannel {
        MOBILE("手机银行"), PC("PC");
        private String value;

        TransferChannel(String value) {
            this.value = value;
        }

        static TransferChannel getRandom() {
            return TransferChannel.values()[(int) (Math.random() * TransferChannel.values().length)];
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private enum TransactionMode {
        TONG_HANG("同行转账"), KUA_HANG("跨行转账"), POS("POS消费");
        private String value;

        TransactionMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private enum TransferMode {
        DUAN_XIN("短信验证码"), UEKY("UKEY");
        private String value;

        TransferMode(String value) {
            this.value = value;
        }

        static TransferMode getRandom() {
            return TransferMode.values()[(int) (Math.random() * TransferMode.values().length)];
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

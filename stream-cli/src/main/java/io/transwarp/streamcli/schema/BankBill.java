package io.transwarp.streamcli.schema;

import io.transwarp.streamcli.common.DataGen;
import io.transwarp.streamcli.Generator;
import io.transwarp.streamcli.common.StringGenerator;
import io.transwarp.streamcli.common.TimeGenerator;

import java.util.StringJoiner;

/**
 * Author: stk
 * Date: 2018/3/6
 * <p>
 * Generate random bank bill information.
 * Format: 转出账号, 转账人姓名, 收款账号, 收款人姓名, 转账金额, 交易类型, 转账渠道, 转账方式, 转账时间, 转账附言, 手续费
 * E.g., 0001000235655286,池澉森,0001000235077040,严铠征,356,转账,跨行转账,PC,UKEY,20111107,,
 */
public class BankBill implements DataGen {
    private final int billBase = Integer.parseInt(Generator.props.getProperty("bank.bill.base"));
    private final double charge = Double.parseDouble(Generator.props.getProperty("bank.bill.charge"));
    private BankAccount bankAccount = new BankAccount();

    @Override
    public String nextRecord() {
        StringJoiner result = new StringJoiner(Generator.delimiter);
        String[] account1 = bankAccount.nextRecord().split(Generator.delimiter);
        String[] account2 = bankAccount.nextRecord().split(Generator.delimiter);

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
        result.add(outCardNum);
        result.add(outPeople);
        result.add(cardNum2);
        result.add(people2);
        double bill = Math.random() * billBase + 1;
        result.add(String.format("%.2f", bill));
        result.add(transactionType);

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
        result.add(transactionMode);
        result.add(transferChannel);
        result.add(transferMode);
        result.add(TimeGenerator.randomDateWithTrans(date1.compareTo(date2) > 0 ? date1 : date2,
                Generator.props.getProperty("bank.bill.date.end"),
                Generator.props.getProperty("bank.bill.date.format")));
        result.add(StringGenerator.randomUpper(10));
        result.add(String.format("%.2f", bill * charge));
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

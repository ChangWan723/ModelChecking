package src.model;

import java.io.Serializable;

public class TransferMessage implements Serializable {
    private final int transferId;
    private final String bankName;
    private final int fromAccountId;
    private final int toAccountId;
    private final long amount;

    public TransferMessage(int transferId, String bankName, int fromAccountId, int toAccountId, long amount) {
        this.transferId = transferId;
        this.bankName = bankName;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public String getBankName() {
        return bankName;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public long getAmount() {
        return amount;
    }
}

package src.model;

import java.io.Serializable;

public class TransferRequest implements Serializable {
    private final int fromAccountId;
    private final int toAccountId;
    private final long amount;

    public TransferRequest(int fromAccountId, int toAccountId, long amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
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

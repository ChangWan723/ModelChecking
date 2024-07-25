package src.model;

public class TransferMessage {
    private final int transferId;
    private final int fromAccountId;
    private final int toAccountId;
    private final long amount;

    public TransferMessage(int transferId, int fromAccountId, int toAccountId, long amount) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
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

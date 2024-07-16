package src.model;

public class TransferMessage {
    private final int transferId;
    private final int fromAccountId;
    private final int toAccountId;
    private final double amount;

    public TransferMessage(int transferId, int fromAccountId, int toAccountId, double amount) {
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

    public double getAmount() {
        return amount;
    }
}

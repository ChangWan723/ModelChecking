package src.model;

public class Message {
    private final int fromAccountId;
    private final int toAccountId;
    private final double amount;

    public Message(int fromAccountId, int toAccountId, double amount) {
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

    public double getAmount() {
        return amount;
    }
}

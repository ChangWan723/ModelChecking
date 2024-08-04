package src.model;

public class Account {
    private final int accountId;
    private long balance;
    private final String backName;

    public Account(int accountId, long initialBalance, String backName) {
        this.accountId = accountId;
        this.balance = initialBalance;
        this.backName = backName;
    }

    public long getBalance() {
        return balance;
    }

    public void deposit(long amount) {
        balance += amount;
    }

    public void withdraw(long amount) {
        balance -= amount;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getBackName() {
        return backName;
    }
}
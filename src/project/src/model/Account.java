package src.model;

public class Account {
    private final int accountId;
    private int balance;

    public Account(int accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public int getAccountId() {
        return accountId;
    }
}
package src.model;

public class Account {
    private final int accountId;
    private double balance;

    public Account(int accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public int getAccountId() {
        return accountId;
    }
}
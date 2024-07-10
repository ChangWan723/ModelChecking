package src.model;

public class Account {
    private final int accountId;
    private double balance;
    private final String backName;

    public Account(int accountId, double initialBalance, String backName) {
        this.accountId = accountId;
        this.balance = initialBalance;
        this.backName = backName;
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
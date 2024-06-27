package src.service;

public interface TransactionManager {
    void transfer(int fromId, int toId, int amount);
}

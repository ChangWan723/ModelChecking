package src.service.transfer;

public interface TransferManager {
    void transfer(int fromId, int toId, long amount);
}

package src.service.transfer;

import src.repository.model.TransferMessage;
import src.service.transfer.server.ExternalBankServer;
import src.service.transfer.server.InternalBankServer;

public class CrossBankTransfer implements TransferManager {
    public CrossBankTransfer() {
        new Thread(new InternalBankServer()).start();
        new Thread(new ExternalBankServer()).start();
    }

    public void transfer(int fromAccountId, int toAccountId, long amount) {
        try {
            MessageQueue.getRequestMessages().put(new TransferMessage(1, fromAccountId, toAccountId, amount));
            String result = MessageQueue.getResultMessages().take();
            System.out.println(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

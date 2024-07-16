package src.service.transfer;

import src.model.TransferMessage;
import src.service.transfer.server.ExternalBankServer;
import src.service.transfer.server.MessageQueue;
import src.service.transfer.server.CrossBankTransHandler;

public class CrossBankTransfer implements TransferManager {
    public CrossBankTransfer() {
        new Thread(new CrossBankTransHandler()).start();
        new Thread(new ExternalBankServer()).start();
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        try {
            MessageQueue.getRequestMessages().put(new TransferMessage(1, fromAccountId, toAccountId, amount));
            String result = MessageQueue.getResultMessages().take();
            System.out.println(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package src.service.transfer;

import src.model.TransferMessage;
import src.service.transfer.server.ExternalBankServer;
import src.service.transfer.server.InternalBankServer;
import src.service.transfer.server.MessageQueueServer;

public class CrossBankTransfer implements TransferManager {
    public CrossBankTransfer() {
        new Thread(new InternalBankServer()).start();
        new Thread(new ExternalBankServer()).start();
        new Thread(new MessageQueueServer()).start();
    }

    public void transfer(int fromAccountId, int toAccountId, long amount) {
        try {
            InternalMessageQueue.getRequestMessages().put(new TransferMessage(1, "Bank B", fromAccountId, toAccountId, amount));
            String result = InternalMessageQueue.getResultMessages().take();
            System.out.println("### result:" + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

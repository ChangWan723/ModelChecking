package src.controller;

import src.repository.ExternalAccountRepo;
import src.repository.InternalAccountRepo;
import src.service.transfer.CrossBankTransfer;
import src.service.transfer.TransferManager;

public class CrossBankTransCtrl {
    public static void main(String[] args) {
        new CrossBankTransCtrl().transferCrossBank(2, 3, 100);
    }

    public void transferCrossBank(int fromAccountId, int toAccountId, long amount) {
        ((TransferManager) new CrossBankTransfer()).transfer(fromAccountId, toAccountId, amount);

        waitForAsyncProcess(1000);
        System.out.println("fromAccount:" + InternalAccountRepo.getInstance().accessAccount(fromAccountId).get().getBalance());
        System.out.println("toAccount:" + ExternalAccountRepo.getInstance().accessAccount(toAccountId).get().getBalance());
    }

    private static void waitForAsyncProcess(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


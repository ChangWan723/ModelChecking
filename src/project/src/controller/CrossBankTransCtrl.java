package src.controller;

import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.transfer.CrossBankTransfer;
import src.service.transfer.TransferManager;

public class CrossBankTransCtrl {
    private final AccountRepo accountRepo;
    private final TransferManager crossBankTransfer;

    public CrossBankTransCtrl() {
        this.accountRepo = DefaultAccountRepo.getInstance();
        this.crossBankTransfer = new CrossBankTransfer();
    }

    public static void main(String[] args) {
        new CrossBankTransCtrl().transferCrossBank(2, 3, 100.0);
    }

    public void transferCrossBank(int fromAccountId, int toAccountId, double amount) {
        crossBankTransfer.transfer(fromAccountId, toAccountId, amount);

        waitForAsyncProcess(1000);
        System.out.println("fromAccount:" + accountRepo.accessAccount(fromAccountId).get().getBalance());
        System.out.println("toAccount:" + accountRepo.accessAccount(toAccountId).get().getBalance());
    }

    private static void waitForAsyncProcess(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


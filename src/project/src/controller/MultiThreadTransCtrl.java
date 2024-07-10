package src.controller;

import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.transfer.SameBankTransfer;
import src.service.transfer.TransferManager;

public class MultiThreadTransCtrl {
    private final AccountRepo accountRepo;
    private final TransferManager simpleTransfer;

    public MultiThreadTransCtrl() {
        this.accountRepo = DefaultAccountRepo.getInstance();
        this.simpleTransfer = new SameBankTransfer();
    }

    public static void main(String[] args) {
        new MultiThreadTransCtrl().multiThreadTrans(1, 2, 10);
    }

    public void multiThreadTrans(int fromId, int toId, double amount) {
        createMultiThread(fromId, toId, amount, 100);

        System.out.println("Final balance of fromId: " + accountRepo.queryBalance(fromId));
        System.out.println("Final balance of toId: " + accountRepo.queryBalance(toId));
    }

    private void createMultiThread(int fromId, int toId, double amount, int transTimes) {
        Thread user1 = new Thread(() -> {
            for (int i = 0; i < transTimes; i++) {
                simpleTransfer.transfer(fromId, toId, amount);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = 0; i < transTimes - 1; i++) {
                simpleTransfer.transfer(toId, fromId, amount);
            }
        });

        user1.start();
        user2.start();

        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
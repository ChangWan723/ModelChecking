package src.controller;

import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;
import src.service.transfer.SameBankTransfer;
import src.service.transfer.TransferManager;

public class MultiThreadTransCtrl {
    private final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    public static void main(String[] args) {
        new MultiThreadTransCtrl().multiThreadTransToEachOther(1, 2, 10);
    }

    public void multiThreadTransToEachOther(int fromId, int toId, long amount) {
        Thread user1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                ((TransferManager) new SameBankTransfer()).transfer(fromId, toId, amount);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                ((TransferManager) new SameBankTransfer()).transfer(toId, fromId, amount);
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

        System.out.println("Final balance of fromId: " + accountRepo.queryBalance(fromId));
        System.out.println("Final balance of toId: " + accountRepo.queryBalance(toId));
    }
}

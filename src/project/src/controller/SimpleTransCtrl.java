package src.controller;

import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.transfer.SimpleTransfer;
import src.service.transfer.TransferManager;

public class SimpleTransCtrl {
    private final AccountRepo accountRepo;
    private final TransferManager simpleTransfer;

    public SimpleTransCtrl() {
        this.accountRepo = DefaultAccountRepo.getInstance();
        this.simpleTransfer = new SimpleTransfer();
    }

    public void transToEachOther(int fromId, int toId, int amount) {
        Thread user1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                simpleTransfer.transfer(fromId, toId, amount);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
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

        System.out.println("Final balance of fromId: " + accountRepo.queryBalance(fromId));
        System.out.println("Final balance of toId: " + accountRepo.queryBalance(toId));
    }
}

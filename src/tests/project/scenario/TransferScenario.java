package project.scenario;

import src.controller.SameBankTransCtrl;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

public class TransferScenario {
    public static final int TRANS_TIMES = 1;
    private static final AccountRepo accountRepo = InternalAccountRepo.getInstance();
    private static final SameBankTransCtrl transCtrl = new SameBankTransCtrl();

    public static void main(String[] args) {
        multiThreadTransToEachOther(1, 2, 10);
    }

    public static void multiThreadTransToEachOther(int fromId, int toId, long amount) {
        Thread user1 = new Thread(() -> {
            for (int i = 0; i < TRANS_TIMES; i++) {
                transCtrl.transferMoney(fromId, toId, amount);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = 0; i < TRANS_TIMES; i++) {
                transCtrl.transferMoney(toId, fromId, amount);
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

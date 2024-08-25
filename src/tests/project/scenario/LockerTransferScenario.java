package project.scenario;

import src.controller.SameBankTransCtrl;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

public class LockerTransferScenario {
    private static final SameBankTransCtrl transCtrl = new SameBankTransCtrl();
    private static final int TRANS_TIMES = 1;

    public static void main(String[] args) {
        transferToEachOtherWithLocker(1, 2, 10);
    }

    public static void transferToEachOtherWithLocker(int fromId, int toId, long amount) {
        Thread user1 = new Thread(() -> {
            for (int i = 0; i < TRANS_TIMES; i++) {
                transCtrl.transferMoneyWithLocker(fromId, toId, amount);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = 0; i < TRANS_TIMES; i++) {
                transCtrl.transferMoneyWithLocker(toId, fromId, amount);
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

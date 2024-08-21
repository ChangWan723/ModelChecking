package project.scenario;

import src.controller.CrossBankTransCtrl;
import src.repository.ExternalAccountRepo;
import src.repository.InternalAccountRepo;

public class CrossBankTransScenario {
    public static void main(String[] args) {
        crossBankTrans(2, 3, 100);
    }

    public static void crossBankTrans(int fromId, int toId, long amount) {
        new CrossBankTransCtrl().transferCrossBank(2, 3, 100);

        waitForAsyncProcess(1000);
        System.out.println("fromAccount:" + InternalAccountRepo.getInstance().accessAccount(fromId).get().getBalance());
        System.out.println("toAccount:" + ExternalAccountRepo.getInstance().accessAccount(toId).get().getBalance());
        System.exit(0);
    }

    private static void waitForAsyncProcess(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

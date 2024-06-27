package project.testcase;

import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.DefaultTransaction;
import src.service.TransactionManager;

public class TransExample {
    private static final AccountRepo accountRepo = DefaultAccountRepo.getInstance();
    private static TransactionManager transactionManager = new DefaultTransaction();


    public static void main(String[] args) {
        int [] result = transExample();
        System.out.println("Final balance of account1: " + result[0]);
        System.out.println("Final balance of account2: " + result[1]);
    }

    public static int[] transExample() {
        Thread user1 = new Thread(() -> {
             for (int i = 0; i < 10; i++) {
                transactionManager.transfer(1, 2, 10);
             }
        });

        Thread user2 = new Thread(() -> {
             for (int i = 0; i < 10; i++) {
                transactionManager.transfer(2, 1, 10);
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

        return new int[] {accountRepo.queryBalance(1), accountRepo.queryBalance(2)};
    }
}

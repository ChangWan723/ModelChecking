package src.service.transfer;

import src.model.Account;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

import java.util.Optional;

public class SameBankTransfer implements TransferManager {
    private final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    public void transfer(int fromId, int toId, long amount) {
        Optional<Account> from = accountRepo.accessAccount(fromId);
        Optional<Account> to = accountRepo.accessAccount(toId);

        if (from.isEmpty() || to.isEmpty() || nonSameBank(from.get(), to.get())) {
            System.out.println("Account does not exist, or account is not in the same bank");
            throw new IllegalArgumentException();
        }

        from.get().withdraw(amount);
        to.get().deposit(amount);
    }

    public void transferWithLocker(int fromId, int toId, long amount) {
        Optional<Account> from = accountRepo.accessAccount(fromId);
        Optional<Account> to = accountRepo.accessAccount(toId);

        if (from.isEmpty() || to.isEmpty() || nonSameBank(from.get(), to.get())) {
            System.out.println("Account does not exist, or account is not in the same bank");
            throw new IllegalArgumentException();
        }
        Account fromAccount = from.get();
        Account toAccount = to.get();

        synchronized (fromAccount) {
            doSomething(50);
            synchronized (toAccount) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
            }
        }
    }

    private void doSomething(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean nonSameBank(Account from, Account to) {
        return !from.getBackName().equals(to.getBackName());
    }
}


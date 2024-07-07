package src.service;

import src.model.Account;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;

import java.util.Optional;

public class DefaultTransfer implements TransferManager {
    private final AccountRepo accountRepo = DefaultAccountRepo.getInstance();

    public void transfer(int fromId, int toId, int amount) {
        Optional<Account> from = accountRepo.accessAccount(fromId);
        Optional<Account> to = accountRepo.accessAccount(toId);

        if (from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException();
        }

        from.get().withdraw(amount);
        to.get().deposit(amount);
    }
}


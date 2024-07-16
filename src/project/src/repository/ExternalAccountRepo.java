package src.repository;

import src.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExternalAccountRepo implements AccountRepo {
    private final List<Account> accountList = new ArrayList<>();

    private ExternalAccountRepo() {
        // Mock some data
        accountList.add(new Account(3, 1000, "BankB"));
        accountList.add(new Account(4, 1000, "BankB"));
    }

    private static final class InstanceHolder {
        private static final ExternalAccountRepo instance = new ExternalAccountRepo();
    }

    public static ExternalAccountRepo getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Optional<Account> accessAccount(int id) {
        return accountList.stream().filter(account -> account.getAccountId() == id).findFirst();
    }

    @Override
    public double queryBalance(int id) {
        Optional<Account> account = accessAccount(id);
        return account.isPresent() ? account.get().getBalance() : 0;
    }
}

package src.repository;

import src.model.Account;

import java.util.Optional;

public interface AccountRepo {
    Optional<Account> accessAccount(int id);

    long queryBalance(int id);
}

package src.controller;

import src.repository.InternalAccountRepo;
import src.service.interest.InterestCalculator;

public class InterestCtrl {
    private final InterestCalculator interestCalculator;

    public InterestCtrl() {
        this.interestCalculator = new InterestCalculator();
    }

    public long calculateCompoundInterest(int accountId, int years) {
        long principal = InternalAccountRepo.getInstance().queryBalance(accountId);

        return interestCalculator.calculateCompoundInterest(principal, years);
    }
}

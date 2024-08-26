package src.controller;

import src.service.transfer.CrossBankTransfer;

public class CrossBankTransCtrl {
    public void transferCrossBank(int fromAccountId, int toAccountId, long amount) {
        new CrossBankTransfer().transfer(fromAccountId, toAccountId, amount);
    }
}


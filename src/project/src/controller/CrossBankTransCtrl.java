package src.controller;

import src.repository.ExternalAccountRepo;
import src.repository.InternalAccountRepo;
import src.service.transfer.CrossBankTransfer;
import src.service.transfer.TransferManager;

public class CrossBankTransCtrl {
    public void transferCrossBank(int fromAccountId, int toAccountId, long amount) {
        ((TransferManager) new CrossBankTransfer()).transfer(fromAccountId, toAccountId, amount);
    }
}


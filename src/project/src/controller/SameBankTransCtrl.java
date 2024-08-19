package src.controller;

import src.service.transfer.SameBankTransfer;

public class SameBankTransCtrl {
    private final SameBankTransfer sameBankTransfer;

    public SameBankTransCtrl() {
        this.sameBankTransfer = new SameBankTransfer();
    }

    public void transferMoney(int fromId, int toId, long amount) {
        sameBankTransfer.transfer(fromId, toId, amount);
    }
}

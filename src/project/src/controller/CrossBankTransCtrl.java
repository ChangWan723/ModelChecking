package src.controller;

import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.transfer.CrossBankTransfer;
import src.service.transfer.server.BankATransferServer;
import src.service.transfer.TransferManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CrossBankTransCtrl {
    private final AccountRepo accountRepo;
    private final TransferManager crossBankTransfer;

    public CrossBankTransCtrl() {
        this.accountRepo = DefaultAccountRepo.getInstance();
        this.crossBankTransfer = new CrossBankTransfer();
    }

    public static void main(String[] args) {
        new CrossBankTransCtrl().transferCrossBank(2, 3, 100.0);
    }

    public void transferCrossBank(int fromAccountId, int toAccountId, double amount) {
        // Start server
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(12345);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new BankATransferServer(clientSocket, accountRepo)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Test transfer
        try {
            crossBankTransfer.transfer(fromAccountId, toAccountId, amount);
        } catch (Exception e1) {
            System.out.println("Transfer initiation failed: " + e1.getMessage());
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("fromAccount:" + accountRepo.accessAccount(fromAccountId).get().getBalance());
        System.out.println("toAccount:" + accountRepo.accessAccount(toAccountId).get().getBalance());
    }
}


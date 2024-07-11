package src.service.transfer;

import src.model.Account;
import src.model.TransferRequest;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;
import src.service.transfer.server.BankATransferServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CrossBankTransfer implements TransferManager {
    private final AccountRepo accountRepo = DefaultAccountRepo.getInstance();
    private final BlockingQueue<TransferRequest> transferQueue = new LinkedBlockingQueue<>();
    private static final int MAX_RETRY_ATTEMPTS = 3;

    public CrossBankTransfer() {
        new Thread(new TransferWorker(this)).start();
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        new Thread(() -> {
            new Thread(new BankATransferServer()).start();
        }).start();

        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, amount);
        try {
            transferQueue.put(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TransferWorker implements Runnable {
        private final CrossBankTransfer crossBankTransfer;

        public TransferWorker(CrossBankTransfer crossBankTransfer) {
            this.crossBankTransfer = crossBankTransfer;
        }

        @Override
        public void run() {
            // Use a while loop to continually fetch transfer messages from the message queue
            while (true) {
                try {
                    TransferRequest request = crossBankTransfer.transferQueue.take();
                    processTransfer(request, 0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processTransfer(TransferRequest request, int attempt) {
            int fromAccountId = request.getFromAccountId();
            int toAccountId = request.getToAccountId();
            double amount = request.getAmount();
            try {
                // Simulate socket communication
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(request);

                // Simulate network issue
                /*if (Math.random() < 0.3) { // 30% chance of failure
                    throw new Exception("Network issue, retrying...");
                }*/

                String response = (String) in.readObject();
                if ("Transfer successful".equals(response)) {
                    System.out.println("Transfer successful: " + amount + " from " + fromAccountId + " to " + toAccountId);
                } else {
                    throw new Exception("Transfer failed on remote bank");
                }
                socket.close();
            } catch (Exception e) {
                System.out.println("Transfer attempt " + (attempt + 1) + " failed: " + e.getMessage());
                if (attempt < MAX_RETRY_ATTEMPTS) {
                    retryTransfer(request, attempt);
                } else {
                    rollbackTransfer(request);
                }
            }
        }

        private void retryTransfer(TransferRequest request, int attempt) {
            try {
                Thread.sleep(500); // Wait before retrying
                processTransfer(request, attempt + 1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void rollbackTransfer(TransferRequest request) {
            try {
                Optional<Account> fromAccount = crossBankTransfer.accountRepo.accessAccount(request.getToAccountId());
                Optional<Account> toAccount = crossBankTransfer.accountRepo.accessAccount(request.getFromAccountId());
                if (fromAccount.isPresent() && toAccount.isPresent()) {
                    fromAccount.get().withdraw(request.getAmount());
                    toAccount.get().deposit(request.getAmount());
                    System.out.println("Rollback successful: " + request.getAmount() + " returned to " + request.getFromAccountId());
                } else {
                    System.out.println("Rollback failed: Account not found");
                }
            } catch (Exception e) {
                System.out.println("Rollback failed: " + e.getMessage());
            }
        }
    }
}

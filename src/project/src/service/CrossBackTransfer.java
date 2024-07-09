package src.service;

import src.model.Account;
import src.model.TransferRequest;
import src.repository.AccountRepo;

import java.io.*;
import java.net.*;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CrossBackTransfer implements TransferManager {
    private final AccountRepo accountRepository;
    private final BlockingQueue<TransferRequest> transferQueue = new LinkedBlockingQueue<>();
    private static final int MAX_RETRY_ATTEMPTS = 3;

    public CrossBackTransfer(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
        new Thread(new TransferWorker()).start();
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, amount);
        try {
            transferQueue.put(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class TransferWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TransferRequest request = transferQueue.take();
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
                // Simulate network issue
                if (Math.random() < 0.3) { // 30% chance of failure
                    throw new Exception("Network issue, retrying...");
                }
                // Simulate socket communication
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(request);
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
                Thread.sleep(2000); // Wait before retrying
                processTransfer(request, attempt + 1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void rollbackTransfer(TransferRequest request) {
            try {
                Optional<Account> fromAccount = accountRepository.accessAccount(request.getToAccountId());
                Optional<Account> toAccount = accountRepository.accessAccount(request.getFromAccountId());
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

package src.service.transfer.server;

import src.model.Account;
import src.model.TransferMessage;
import src.model.TransferRequest;
import src.repository.DefaultAccountRepo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;


public class CrossBankTransferHandler implements Runnable {
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Override
    public void run() {
        // Use a while loop to continually fetch transfer messages from the message queue
        try {
            TransferMessage message = MessageQueue.getTransferQueue().take();

            Optional<Account> account = DefaultAccountRepo.getInstance().accessAccount(message.getFromAccountId());
            if (account.isEmpty()) {
                System.out.println("Account does not exist");
                throw new IllegalArgumentException();
            }
            account.get().withdraw(message.getAmount());

            processTransfer(new TransferRequest(message.getToAccountId(), message.getAmount()), 0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processTransfer(TransferRequest request, int attempt) {
        Optional<Account> account = DefaultAccountRepo.getInstance().accessAccount(request.getAccountId());
        if (account.isEmpty()) {
            System.out.println("Account does not exist");
            throw new IllegalArgumentException();
        }

        try (Socket socket = new Socket("localhost", 12345)) {
            // Simulate network communication
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(request);

            // Simulate network issue
/*            if (true || Math.random() < 0.3) { // 30% chance of failure
                throw new Exception("Network issue, retrying...");
            }*/

            String response = (String) in.readObject();
            if ("Transfer successful".equals(response)) {
                System.out.println("Transfer successful");
            } else {
                throw new Exception("Transfer failed on remote bank");
            }
        } catch (Exception e) {
            System.out.println("Transfer attempt " + (attempt + 1) + " failed: " + e.getMessage());
            if (attempt < MAX_RETRY_ATTEMPTS) {
                retryTransfer(request, attempt);
            } else {
                rollbackTransfer(request);
            }
        }
    }

    private static Optional<Socket> createSocket(String bankName) throws IOException {
        if (bankName.equals("BankA")) {
            return Optional.of(new Socket("localhost", 12345));
        } else if (bankName.equals("BankB")) {
            return Optional.of(new Socket("localhost", 12346));
        }

        return Optional.empty();
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
            Optional<Account> account = DefaultAccountRepo.getInstance().accessAccount(request.getAccountId());
            if (account.isPresent()) {
                account.get().deposit(request.getAmount());
                System.out.println("Rollback successful: " + request.getAmount() + " returned to " + request.getAccountId());
            } else {
                System.out.println("Rollback failed: Account not found");
            }
        } catch (Exception e) {
            System.out.println("Rollback failed: " + e.getMessage());
        }
    }
}


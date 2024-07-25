package src.service.transfer.server;

import src.model.Account;
import src.model.TransferMessage;
import src.model.TransferRequest;
import src.repository.InternalAccountRepo;
import src.service.transfer.MessageQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.Random;


public class InternalBankServer implements Runnable {
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Override
    public void run() {
        try {
            TransferMessage message = MessageQueue.getRequestMessages().take();

            withdrawFromInternalBank(message);

            crossBankTransfer(message, 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void withdrawFromInternalBank(TransferMessage message) {
        Optional<Account> account = InternalAccountRepo.getInstance().accessAccount(message.getFromAccountId());
        if (!account.isPresent()) {
            System.out.println("Account does not exist");
            throw new IllegalArgumentException();
        }
        account.get().withdraw(message.getAmount());
    }

    private void crossBankTransfer(TransferMessage message, int attempt) {
        try {
            simulateNetwork();

            // Simulate network communication
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(new TransferRequest(message.getFromAccountId(), message.getToAccountId(), message.getAmount()));
            String response = (String) in.readObject();
            if ("Transfer successful".equals(response)) {
                MessageQueue.getResultMessages().put("transferId " + message.getTransferId() + ": Success");
            } else {
                throw new Exception("Transfer failed on remote bank");
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Transfer attempt " + attempt + " failed: " + e.getMessage());
            if (attempt < MAX_RETRY_ATTEMPTS) {
                retryTransfer(message, attempt);
            } else {
                rollbackTransfer(message);
            }
        }
    }

    private static void simulateNetwork() throws Exception {
        // Simulate network issue (50% chance of failure)
        if (new Random().nextInt(10) < 0) {
            throw new Exception("Network issue, retrying...");
        }
    }

    private void retryTransfer(TransferMessage message, int attempt) {
        try {
            Thread.sleep(100); // Wait before retrying
            crossBankTransfer(message, attempt + 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void rollbackTransfer(TransferMessage message) {
        returnMoneyToInternalBank(message);

        try {
            MessageQueue.getResultMessages().put("transferId " + message.getTransferId() + ": Fail");
        } catch (InterruptedException e) {
            System.out.println("Rollback failed: " + e.getMessage());
        }

    }

    private static void returnMoneyToInternalBank(TransferMessage message) {
        Optional<Account> account = InternalAccountRepo.getInstance().accessAccount(message.getFromAccountId());
        if (!account.isPresent()) {
            System.out.println("Rollback failed: Account not found");
            return;
        }
        account.get().deposit(message.getAmount());
        System.out.println("Rollback successful: " + message.getAmount() + " returned to " + message.getFromAccountId());
    }
}


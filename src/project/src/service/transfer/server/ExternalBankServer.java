package src.service.transfer.server;

import src.model.Account;
import src.model.TransferRequest;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class ExternalTransferServer implements Runnable {
    private final AccountRepo accountRepository = DefaultAccountRepo.getInstance();

    @Override
    public void run() {
        try (Socket socket = new ServerSocket(12345).accept()) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            TransferRequest request = (TransferRequest) in.readObject();
            processTransfer(request);
            out.writeObject("Transfer successful");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processTransfer(TransferRequest request) {
        try {
            Optional<Account> account = accountRepository.accessAccount(request.getAccountId());
            if (account.isEmpty()) {
                throw new Exception("Account not found");
            }
            account.get().deposit(request.getAmount());
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}

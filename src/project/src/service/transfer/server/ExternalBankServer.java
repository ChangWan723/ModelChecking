package src.service.transfer.server;

import src.repository.model.Account;
import src.repository.model.TransferRequest;
import src.repository.AccountRepo;
import src.repository.ExternalAccountRepo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class ExternalBankServer implements Runnable {
    private final AccountRepo accountRepository = ExternalAccountRepo.getInstance();
    private static final int TIMEOUT = 3000;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            serverSocket.setSoTimeout(TIMEOUT);
            Socket socket = serverSocket.accept();
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
            Optional<Account> account = accountRepository.accessAccount(request.getToAccountId());
            if (!account.isPresent()) {
                throw new Exception("Account not found");
            }
            account.get().deposit(request.getAmount());
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}

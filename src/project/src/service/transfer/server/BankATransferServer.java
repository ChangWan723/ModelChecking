package src.service.transfer.server;

import src.model.Account;
import src.model.TransferRequest;
import src.repository.AccountRepo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class BankATransferServer implements Runnable {
    private final Socket socket;
    private final AccountRepo accountRepository;

    public BankATransferServer(Socket socket, AccountRepo accountRepository) {
        this.socket = socket;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            TransferRequest request = (TransferRequest) in.readObject();
            processTransfer(request);
            out.writeObject("Transfer successful");
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processTransfer(TransferRequest request) {
        int fromAccountId = request.getFromAccountId();
        int toAccountId = request.getToAccountId();
        double amount = request.getAmount();
        try {
            Optional<Account> fromAccount = accountRepository.accessAccount(fromAccountId);
            Optional<Account> toAccount = accountRepository.accessAccount(toAccountId);
            if (fromAccount.isEmpty() || toAccount.isEmpty()) {
                throw new Exception("Account not found");
            }
            fromAccount.get().withdraw(amount);
            toAccount.get().deposit(amount);
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}

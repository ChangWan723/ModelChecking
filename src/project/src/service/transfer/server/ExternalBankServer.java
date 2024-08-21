package src.service.transfer.server;

import src.model.Account;
import src.model.TransferMessage;
import src.repository.AccountRepo;
import src.repository.ExternalAccountRepo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ExternalBankServer implements Runnable {
    public static final String BANK_NAME = "Bank B";
    private final AccountRepo accountRepository = ExternalAccountRepo.getInstance();

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 12346);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            while (true) {
                Thread.sleep(500);

                out.writeObject(BANK_NAME);
                out.flush();

                Object response = in.readObject();
                TransferMessage message = (TransferMessage) response;
                processTransfer(message);
            }
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    private void processTransfer(TransferMessage request) {
        try {
            Optional<Account> account = accountRepository.accessAccount(request.getToAccountId());
            if (account.isEmpty()) {
                throw new Exception("Account not found");
            }
            account.get().deposit(request.getAmount());
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}

package src.service.transfer.server;

import src.model.TransferMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueueServer implements Runnable {
    private final BlockingQueue<TransferMessage> messageQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void run() {
        new Thread(new ProducerListener()).start();
        new Thread(new ConsumerListener()).start();
    }

    class ProducerListener implements Runnable {
        @Override
        public void run() {
            try (ServerSocket producerSocket = new ServerSocket(12345)) {
                while (true) {
                    Socket producerConnection = producerSocket.accept();
                    executorService.submit(new ProducerHandler(producerConnection, messageQueue));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ConsumerListener implements Runnable {
        @Override
        public void run() {
            try (ServerSocket consumerSocket = new ServerSocket(12346)) {
                while (true) {
                    Socket consumerConnection = consumerSocket.accept();
                    executorService.submit(new ConsumerHandler(consumerConnection, messageQueue));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ProducerHandler implements Runnable {
    private final Socket socket;
    private final BlockingQueue<TransferMessage> messageQueue;

    public ProducerHandler(Socket socket, BlockingQueue<TransferMessage> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            TransferMessage message = (TransferMessage) in.readObject();
            messageQueue.put(message);
            out.writeObject("MQ successful");
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ConsumerHandler implements Runnable {
    private final Socket socket;
    private final BlockingQueue<TransferMessage> messageQueue;

    public ConsumerHandler(Socket socket, BlockingQueue<TransferMessage> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            String backName = (String) in.readObject();

            TransferMessage matchedMessage = null;
            for (TransferMessage message : messageQueue) {
                if (message.getBankName().equals(backName)) {
                    matchedMessage = message;
                    break;
                }
            }

            if (matchedMessage != null) {
                messageQueue.remove(matchedMessage);
                out.writeObject(matchedMessage);
                System.out.println("Sent to consumer: " + matchedMessage.getToAccountId());
            } else {
                out.writeObject(null);
                System.out.println("No matching message found for backName: " + backName);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

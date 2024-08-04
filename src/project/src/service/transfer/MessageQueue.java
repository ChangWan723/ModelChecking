package src.service.transfer;

import src.repository.model.TransferMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    private static final BlockingQueue<TransferMessage> REQUEST_MESSAGES = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> RESULT_MESSAGES = new LinkedBlockingQueue<>();

    public static BlockingQueue<TransferMessage> getRequestMessages() {
        return REQUEST_MESSAGES;
    }

    public static BlockingQueue<String> getResultMessages() {
        return RESULT_MESSAGES;
    }
}

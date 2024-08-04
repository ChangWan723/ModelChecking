package project.scenario;

import src.model.User;
import src.service.users.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserScenario {
    private static final UserService userService = new UserService();
    public static final int THREAD_COUNT = 5;

    public static void concurrentUserCreation(int times) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < times; i++) {
            final int finalI = i;
            executorService.execute(() -> {
                User newUser = new User( "user" + finalI, "password" + finalI);
                userService.registerUser(newUser);
            });
        }
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

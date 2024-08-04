package project.scenario;

import src.model.User;
import src.service.users.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.UUID;

public class UserScenario {
    private static final UserService userService = new UserService();
    public static final int THREAD_COUNT = 2;

    public static void concurrentUserCreationByThreadPool(int times) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(times);

        for (int i = 0; i < times; i++) {
            executorService.execute(() -> {
                try {
                    User newUser = new User("user" + UUID.randomUUID(), "password");
                    userService.registerUser(newUser);
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void createUsersWithTwoThreads(int times) {

        Thread user1 = new Thread(() -> {
            for (int i = 0; i < times / 2; i++) {
                User newUser = new User("user" + i, "password" + i);
                userService.registerUser(newUser);
            }
        });

        Thread user2 = new Thread(() -> {
            for (int i = times / 2; i < times; i++) {
                User newUser = new User("user" + i, "password" + i);
                userService.registerUser(newUser);
            }
        });

        user1.start();
        user2.start();

        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}

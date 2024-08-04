package src.repository;

import src.model.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static src.service.users.EncryptUtil.encryptPassword;

public class UserRepo {
    private final ConcurrentHashMap<Long, User> userTable = new ConcurrentHashMap<>();
    private Long userIdSequence;

    private UserRepo() {
        User user1 = new User( "defaultUser1", encryptPassword("password1"));
        User user2 = new User( "defaultUser2", encryptPassword("password2"));
        user1.setUserId(1L);
        userTable.put(1L, user1);
        user1.setUserId(2L);
        userTable.put(2L, user2);

        userIdSequence = userTable.keySet().stream().max(Long::compare).orElse(1L) + 1;
    }

    private static class UserRepoHolder {
        private static final UserRepo INSTANCE = new UserRepo();
    }

    // 提供全局访问点
    public static UserRepo getInstance() {
        return UserRepoHolder.INSTANCE;
    }

    public int getUserCount() {
        return userTable.size();
    }

    public User save(User user) {
        long userId = userIdSequence++;
        user.setUserId(userId);
        userTable.put(userId, user);
        return user;
    }

    public User findByUserName(String userName) {
        return userTable.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}


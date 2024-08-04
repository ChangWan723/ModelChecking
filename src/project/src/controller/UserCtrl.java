package src.controller;

import src.model.User;
import src.service.users.UserService;

public class UserCtrl {
    private final UserService userService;

    public UserCtrl() {
        this.userService = new UserService();
    }

    public User registerUser(User user) {
        return userService.registerUser(user);
    }

    public User loginUser(String userName, String password) {
        return userService.loginUser(userName, password);
    }
}


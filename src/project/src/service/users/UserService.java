package src.service.users;

import src.model.User;
import src.repository.UserRepo;

import static src.service.users.EncryptUtil.encryptPassword;

public class UserService {
    private final UserRepo userRepo;

    public UserService() {
        this.userRepo = UserRepo.getInstance();
    }

    public User registerUser(User user) {
        if (userRepo.findByUserName(user.getUserName()) != null) {
            throw new RuntimeException("User already exists!");
        }

        String encryptedPassword = encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepo.save(user);
    }

    public User loginUser(String userName, String password) {
        User user = userRepo.findByUserName(userName);
        if (user == null || !user.getPassword().equals(encryptPassword(password))) {
            throw new RuntimeException("Invalid username or password!");
        }
        return user;
    }
}


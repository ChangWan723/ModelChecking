package project.testing;

import org.junit.Assert;
import org.junit.Test;
import project.scenario.UserScenario;
import src.repository.UserRepo;


public class UserCtrlTest {
    private final UserRepo userRepo = UserRepo.getInstance();

    @Test
    public void testConcurrentRegisterUser() {
        int originalCount = userRepo.getUserCount();
        int creationCount = 20;

        UserScenario.concurrentUserCreation(creationCount);

        Assert.assertEquals(originalCount + creationCount, userRepo.getUserCount());
    }
}

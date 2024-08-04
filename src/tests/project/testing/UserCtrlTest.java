package project.testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import project.scenario.UserScenario;
import src.repository.UserRepo;


@RunWith(Parameterized.class)
public class UserCtrlTest {
    private final UserRepo userRepo = UserRepo.getInstance();
    int originalCount = 2;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1][0];
    }
    @Test
    public void testConcurrentRegisterUser() {
        int creationCount = 20;

        UserScenario.concurrentUserCreationByThreadPool(creationCount);

        Assert.assertEquals(originalCount + creationCount, userRepo.getUserCount());
    }
}

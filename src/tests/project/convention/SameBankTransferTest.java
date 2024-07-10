package project.convention;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import src.controller.MultiThreadTransCtrl;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;

@RunWith(Parameterized.class)
public class SameBankTransferTest {
    private static final AccountRepo accountRepo = DefaultAccountRepo.getInstance();

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1][0];
    }

    @Test
    public void testMultiThreadTrans() {
        new MultiThreadTransCtrl().multiThreadTrans(1, 2, 100);

        Assert.assertEquals(900, accountRepo.queryBalance(1), 0.000);
        Assert.assertEquals(1100, accountRepo.queryBalance(2), 0.000);
    }
}

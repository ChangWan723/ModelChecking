package project.testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import project.scenario.LockerTransferScenario;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

@RunWith(Parameterized.class)
public class LockerTransTest {
    private static final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }

    @Test(timeout = 1000) // How much time is appropriate?
    public void testMultiThreadTrans() {
        LockerTransferScenario.transferToEachOtherWithLocker(1, 2, 100);

        Assert.assertEquals(1000, accountRepo.queryBalance(1));
        Assert.assertEquals(1000, accountRepo.queryBalance(2));
    }
}

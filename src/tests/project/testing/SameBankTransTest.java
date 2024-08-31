package project.testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import project.scenario.TransferScenario;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

@RunWith(Parameterized.class)
public class SameBankTransTest {
    private static final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[50][0];
    }

    @Test
    public void testMultiThreadTrans() {
        TransferScenario.multiThreadTransToEachOther(1, 2, 100);

        Assert.assertEquals(1000, accountRepo.queryBalance(1));
        Assert.assertEquals(1000, accountRepo.queryBalance(2));
    }
}

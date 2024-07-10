package project.convention;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import src.controller.SimpleTransCtrl;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;

@RunWith(Parameterized.class)
public class SimpleTransferTest {
    private static final AccountRepo accountRepo = DefaultAccountRepo.getInstance();

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1][0];
    }

    @Test
    public void testTrans() {
        new SimpleTransCtrl().transToEachOther(1, 2, 10);

        Assert.assertEquals(1000, accountRepo.queryBalance(1), 0.000);
        Assert.assertEquals(1000, accountRepo.queryBalance(2), 0.000);
    }
}

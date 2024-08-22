package project.testing;

import org.junit.Assert;
import org.junit.Test;
import project.scenario.CrossBankTransScenario;
import src.repository.AccountRepo;
import src.repository.ExternalAccountRepo;
import src.repository.InternalAccountRepo;

public class CrossBankTransTest {
    private static final AccountRepo internalRepo = InternalAccountRepo.getInstance();
    private static final AccountRepo externalRepo = ExternalAccountRepo.getInstance();

    @Test
    public void testCrossBankTrans() {
        CrossBankTransScenario.crossBankTrans(2, 3, 100);

        Assert.assertEquals(900, internalRepo.queryBalance(2));
        Assert.assertEquals(1100, externalRepo.queryBalance(3));
    }
}
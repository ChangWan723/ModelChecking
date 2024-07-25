package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import src.controller.CrossBankTransCtrl;
import src.repository.AccountRepo;
import src.repository.ExternalAccountRepo;
import src.repository.InternalAccountRepo;

public class CrossBankTransTest extends TestJPF {
    private static final AccountRepo internalRepo = InternalAccountRepo.getInstance();
    private static final AccountRepo externalRepo = ExternalAccountRepo.getInstance();


    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector"
    };

    @Test
    public void testCrossBankTrans_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            new CrossBankTransCtrl().transferCrossBank(2, 3, 100);
        }
    }

    @Test
    public void testMultiThreadTrans_by_assertion() {
        new CrossBankTransCtrl().transferCrossBank(2, 3, 100);

        assertTrue(internalRepo.queryBalance(2) == 900);
        assertTrue(externalRepo.queryBalance(3) == 1100);
    }
}
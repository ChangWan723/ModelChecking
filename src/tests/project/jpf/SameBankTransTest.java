package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import project.scenario.TransferScenario;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;


public class SameBankTransTest extends TestJPF {
    private static final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector"
    };

    @Test
    public void testMultiThreadTrans_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            TransferScenario.multiThreadTransToEachOther(1, 2, 10);
        }
    }

    @Test
    public void testMultiThreadTrans_by_assertion() {
        TransferScenario.multiThreadTransToEachOther(1, 2, 10);

        assertTrue(accountRepo.queryBalance(1) == 1000);
        assertTrue(accountRepo.queryBalance(2) == 1000);
    }
}


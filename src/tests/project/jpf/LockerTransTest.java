package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import project.scenario.LockerTransferScenario;
import project.scenario.TransferScenario;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;

public class LockerTransTest extends TestJPF {
    @Test
    public void testMultiThreadTrans_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation()) {
            LockerTransferScenario.transferToEachOtherWithLocker(1, 2, 10);
        }
    }
}

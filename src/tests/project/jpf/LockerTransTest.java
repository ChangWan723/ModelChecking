package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import project.scenario.LockerTransferScenario;

public class LockerTransTest extends TestJPF {

    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.DeadlockAnalyzer"
    };
    @Test
    public void testMultiThreadTrans_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            LockerTransferScenario.transferToEachOtherWithLocker(1, 2, 10);
        }
    }
}

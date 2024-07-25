package project.jpf;

import gov.nasa.jpf.util.TypeRef;
import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import src.controller.MultiThreadTransCtrl;
import src.repository.AccountRepo;
import src.repository.InternalAccountRepo;


public class SameBankTransTest extends TestJPF {
    private static final AccountRepo accountRepo = InternalAccountRepo.getInstance();

    static final TypeRef PROPERTY = new TypeRef("gov.nasa.jpf.listener.PreciseRaceDetector");
    static final String LISTENER = "+listener=gov.nasa.jpf.listener.PreciseRaceDetector";

    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector",
            "+classpath=build/project"
    };

    @Test
    public void testMultiThreadTrans_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            new MultiThreadTransCtrl().multiThreadTransToEachOther(1, 2, 10);
        }
    }

    @Test
    public void testMultiThreadTrans_by_assertion() {
        new MultiThreadTransCtrl().multiThreadTransToEachOther(1, 2, 10);

        assertTrue(accountRepo.queryBalance(1) == 1000);
        assertTrue(accountRepo.queryBalance(2) == 1000);
    }
}

